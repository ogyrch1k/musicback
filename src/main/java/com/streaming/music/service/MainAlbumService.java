package com.streaming.music.service;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.streaming.music.dto.AlbumData;
import com.streaming.music.dto.TrackData;
import com.streaming.music.model.Album;
import com.streaming.music.model.Executor;
import com.streaming.music.model.Track;
import com.streaming.music.model.User;
import com.streaming.music.repository.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.streaming.music.Utilities.FilesUtil.*;
import static com.streaming.music.Utilities.Hash.*;

@Transactional
@Service("album_service")
@Log
public class MainAlbumService implements AlbumService{
    @Resource(name="trackService")
    private TrackService trackService;
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ExecutorRepository executorRepository;
    private final UserRepository userRepository;


    private final GenreRepository genreRepository;
    @Value("${upload.path.track}")
    private String uploadPath;

    @Value("${upload.path.temp}")
    private String uploadTempPath;
    @Value("${upload.path.image}")
    private String uploadImagePath;
    public MainAlbumService(TrackRepository trackRepository, AlbumRepository albumRepository, ExecutorRepository executorRepository, UserRepository userRepository, GenreRepository genreRepository) {
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
        this.executorRepository = executorRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public AlbumData saveAlbum(AlbumData albumData) {
        Album album = populateAlbumEntity(albumData);
        log.info("Added album "+ albumData.getNameOfAlbum());
        return populateAlbumData(albumRepository.save(album));
    }

    @Override
    public int deleteAlbumById(Integer id) {
        trackRepository.deleteAllByTrackOfAlbum(albumRepository.getAlbumById(id).orElseThrow(()-> new EntityNotFoundException("Album not found")));
        log.info("Removed album "+ albumRepository.getAlbumById(id).orElseThrow(()-> new EntityNotFoundException("Album not found")).getNameOfAlbum());
        return albumRepository.deleteAlbumById(id).orElseThrow(()->new EntityNotFoundException("Album not found"));
    }

    @Override
    public AlbumData getAlbumById(Integer id) {
        return populateAlbumData(albumRepository.getAlbumById(id).orElseThrow(()-> new EntityNotFoundException("Album not found")));
    }

    @Override
    public AlbumData updateAlbum(AlbumData albumData) {
        Album album = populateAlbumEntity(albumData);

        return populateAlbumData(albumRepository.save(album));
    }

    @Override
    public String savePicture(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        String hashName = hashOfMultipartFile(file);
        String resultFilename = saveMultipartFile(file,uploadPath,hashName);
        return resultFilename;
    }

    @Override
    public String unZipAndSavePicture(MultipartFile file,int idAlbum) throws IOException, InvalidDataException, UnsupportedTagException, NoSuchAlgorithmException {
        String currentDirectoryPath = FileSystems.getDefault().
                getPath("").
                toAbsolutePath().
                toString();
        currentDirectoryPath = currentDirectoryPath.concat("/");
        file.transferTo(new File(currentDirectoryPath+uploadTempPath +"/"+"temp.zip"));
        String fileZip = currentDirectoryPath+uploadTempPath +"/"+"temp.zip";
        File destDir = new File(currentDirectoryPath+uploadTempPath);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        String file1="";
        String hashName =  "";
        int i=0;
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs() && i==0) {
                    throw new IOException("Failed to create directory " + newFile);
                }

                i++;
            } else {
                 //fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }
                file1=newFile.toString();
                log.info(file1);
                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                TrackData t = new TrackData();
                hashName = hashOfFile(newFile);
                saveFile(newFile,uploadPath,hashName);
                t.setHashOfTrack(hashName);
                t.setNameOfTrack(newFile.getName().substring(0, newFile.getName().length() - 4));
                t.setGenre(albumRepository.getAlbumById(idAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found")).getGenre());
                TrackData track = trackService.addTrack(t);
                addTrackToAlbum(idAlbum,track.getId());
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        Mp3File song = new Mp3File(getFile(hashName,uploadPath));
        String imageName="";
        if (song.hasId3v2Tag()){
            ID3v2 id3v2tag = song.getId3v2Tag();
            byte[] imageData = id3v2tag.getAlbumImage();
            //converting the bytes to an image
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            String hash = hashOfBytes(bytes);
            File outputfile = new File("image.jpg");
            ImageIO.write(img, "jpg", outputfile);
            imageName = saveFile(outputfile,uploadImagePath,hash);
        }
        return imageName;
    }
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
    @Override
    public AlbumData getAlbumByName(String name) {
        return populateAlbumData(albumRepository.getAlbumByNameOfAlbum(name).orElseThrow(()-> new EntityNotFoundException("Album not found")));
    }

    private AlbumData populateAlbumData(final Album album){
        AlbumData albumData = new AlbumData();
        albumData.setId(album.getId());
        albumData.setNameOfAlbum(album.getNameOfAlbum());
        albumData.setHashOfAvatar(album.getHashOfAvatar());
        albumData.setDateOfCreate(album.getDateOfCreate());
        albumData.setDescriptionText(album.getDescriptionText());
        albumData.setGenre(album.getGenre());
        return albumData;
    }

    private Album populateAlbumEntity(AlbumData albumData){
        Album album;
        try{
            album = albumRepository.getAlbumById(albumData.getId()).orElseThrow(()->new EntityNotFoundException("Album not found"));
            album.setNameOfAlbum(albumData.getNameOfAlbum());
            album.setDateOfCreate(albumData.getDateOfCreate());
            album.setDescriptionText(albumData.getDescriptionText());
            album.setHashOfAvatar(albumData.getHashOfAvatar());
            album.setGenre(albumData.getGenre());
        }catch (Exception ignored){

             album = new Album();
            album.setNameOfAlbum(albumData.getNameOfAlbum());
            try{
                album.setId(albumData.getId());
            }catch (Exception ignored1){

            }
            album.setDateOfCreate(albumData.getDateOfCreate());
            album.setDescriptionText(albumData.getDescriptionText());
            album.setHashOfAvatar(albumData.getHashOfAvatar());
            album.setGenre(albumData.getGenre());
        }

        return album;
    }

    @Override
    public int addTrackToAlbum(int idOfOAlbum, int... idOftrack) {
        Album album = albumRepository.getById(idOfOAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found"));
        List<Track> list = trackRepository.getAllByTrackOfAlbum(album);
        for (int id:idOftrack) {
            list.add(trackRepository.getTrackById(id));
        }
        album.setTracksOfAlbums(list);
        albumRepository.save(album);
        log.info("Track "+list.get(list.size() - 1).getNameOfTrack()+" added to album "+album.getNameOfAlbum());
        return 1;
    }

    @Override
    public int removeTrackOfAlbum(int idOfOAlbum, int... idOftrack){
        Album album = albumRepository.getById(idOfOAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found"));
        List<Track> list = trackRepository.getAllByTrackOfAlbum(album);
        for (int id:idOftrack) {
            list.remove(trackRepository.getTrackById(id));
            trackRepository.deleteById(id);
        }
        album.setTracksOfAlbums(list);
        albumRepository.save(album);
        log.info("Track "+list.get(list.size() - 1).getNameOfTrack()+" removed from album "+album.getNameOfAlbum());
        return 1;
    }

    @Override
    public List<AlbumData> getAlbumByExecutor(int id) {
        List<AlbumData> albumData = new ArrayList<>();
        List<Album> albums = albumRepository.getAllByAlbumsOFExecutor(executorRepository.getById(id)).orElseThrow(()-> new EntityNotFoundException("Albums not found"));
        for (Album w:albums) {
            albumData.add(populateAlbumData(w));
        }
        return albumData;
    }

    @Override
    public List<AlbumData> getAll() {
        List<AlbumData> albumData= new ArrayList<>();
        List<Album> albums = albumRepository.findAll();
        for (Album w:
             albums) {
            albumData.add(populateAlbumData(w));
        }
        return albumData;
    }

    @Override
    public int addAlbumToUser(int idAlbum, int idUser) {
        Album album =albumRepository.getAlbumById(idAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found"));
        List<User> users =userRepository.getAllByUsersAlbums(album).orElseThrow(()-> new EntityNotFoundException("Users not found"));
        users.add(userRepository.getById(idUser).orElseThrow(()-> new EntityNotFoundException("User not found")));
        album.setUsersAlbums(users);
        albumRepository.save(album);
        return 1;
    }

    @Override
    public int removeAlbumFromUser(int idAlbum, int idUser) {
        Album album =albumRepository.getAlbumById(idAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found"));
        List<User> users =userRepository.getAllByUsersAlbums(album).orElseThrow(()-> new EntityNotFoundException("Users not found"));
        users.remove(userRepository.getById(idUser).orElseThrow(()-> new EntityNotFoundException("User not found")));
        album.setUsersAlbums(users);
        albumRepository.save(album);
        return 1;
    }

    @Override
    public int addAlbumToExecutor(int idAlbum, int idOfExecutor) {
        Album album=albumRepository.getAlbumById(idAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found"));
        List<Executor> executors = executorRepository.getAllByExecutorsAlbums(albumRepository.getAlbumById(idAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found")));
        executors.add(executorRepository.getById(idOfExecutor));
        List<Track> tracks = trackRepository.getAllByTrackOfAlbum(albumRepository.getAlbumById(idAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found")));
        for(Track t:tracks){
            trackService.addTrackToExecutor(t.getId(),idOfExecutor);
        }
        album.setAlbumsOFExecutor((List<Executor>) executors);
        albumRepository.save(album);
        return 1;
    }

    @Override
    public int removeAlbumFromExecutor(int idAlbum, int idOfExecutor) {
        Album album=albumRepository.getAlbumById(idAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found"));
        List<Executor> executors = executorRepository.getAllByExecutorsAlbums(albumRepository.getAlbumById(idAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found")));
        executors.remove(executorRepository.getById(idOfExecutor));
        List<Track> tracks = trackRepository.getAllByTrackOfAlbum(albumRepository.getAlbumById(idAlbum).orElseThrow(()-> new EntityNotFoundException("Album not found")));
        for(Track t:tracks){
            trackService.removeTrackFromExecutor(t.getId(),idOfExecutor);
        }
        album.setAlbumsOFExecutor((List<Executor>) executors);
        albumRepository.save(album);
        return 1;
    }


//    @Override
//    public AlbumData getAlbumByNameOfAlbumAndAlbumsOFExecutor(String nameOfAlbum, List<Executor> albumsOFExecutor) {
//
//        return null;
//    }


}
