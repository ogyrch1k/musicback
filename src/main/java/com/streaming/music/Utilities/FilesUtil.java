package com.streaming.music.Utilities;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
@Log
public class FilesUtil {
    public static void makeDir(String path) {
        File outDir = new File(path);
        if (!outDir.exists()) {
            outDir.mkdir();
        }
    }
    public static String saveMultipartFile(MultipartFile file, String uploadPath, String hashName) throws IOException {
        String resultFilename = null;
        String currentDirectoryPath = FileSystems.getDefault().
                getPath("").
                toAbsolutePath().
                toString();
        currentDirectoryPath = currentDirectoryPath.concat("/");
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(currentDirectoryPath+"/"+uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            resultFilename = hashName.substring(hashName.length()-2) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            String res=hashToPathAndMakeDir(hashName,uploadPath) + resultFilename;
            file.transferTo(new File(res));
            return hashName;
        }
        return hashName;
    }
    public static String saveFile(File file, String uploadPath, String hashName) throws IOException {
        String resultFilename = null;
        String currentDirectoryPath = FileSystems.getDefault().
                getPath("").
                toAbsolutePath().
                toString();
        currentDirectoryPath = currentDirectoryPath.concat("/");
        if (file != null && !file.getName().isEmpty()) {
            File uploadDir = new File(currentDirectoryPath+"/"+uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            resultFilename = hashName.substring(hashName.length()-2) + "." + FilenameUtils.getExtension(file.getName());
            String res=hashToPathAndMakeDir(hashName,uploadPath) + resultFilename;
            log.severe(res);
            File file2 = new File(res);
            Files.copy(file.toPath(), Path.of(res), StandardCopyOption.REPLACE_EXISTING);
            log.severe(String.valueOf(file.renameTo(file2)));
            return hashName;
        }
        return hashName;
    }

    public static String hashToPathAndMakeDir(String checksum, String uploadPath){
        String path = FileSystems.getDefault().
                getPath("").
                toAbsolutePath().
                toString();
        path = path.concat(uploadPath);
        path = path.concat("/");
        for(int i =0; i< checksum.length()-2; i++){
            path = path.concat(String.join("",String.valueOf(checksum.charAt(i)),String.valueOf(checksum.charAt(i+1))));
            path = path.concat("/");
            i++;
            makeDir(path);
        }
        return path;
    }
    public static String findFile(String path, String name){
        File directory = new File(path);
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().startsWith(name)) {
                return file.getName();
            }
        }
        return null;
    }

    private static String hashToPath(String hash){
        for (int i=62;i>0;i-=2){
            hash = new StringBuffer(hash).insert(hash.length()-i, "/").toString();
        }
        return hash;
    }
    public static String getFile(String hash, String uploadPath){
        String path=FileSystems.getDefault().
                getPath("").
                toAbsolutePath().
                toString();
        path=path.concat(uploadPath);
        path=path.concat("/");
        path=path.concat(hashToPath(hash));
        String newString = path.substring(0, path.length() - 3);
        String fileName= hash.substring(hash.length() - 2);
        String fullFileName =findFile(newString,fileName);
        newString=newString.concat("/");
        newString=newString.concat(fullFileName);
        return  newString;
    }
}
