package com.streaming.music.Utilities;


import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Hash {
    public String getSold(String login) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                login.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }
    public String getHashOfPassword(String sold,String pass) throws NoSuchAlgorithmException {
        String passSold =sold+pass;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
                passSold.getBytes(StandardCharsets.UTF_8));
        return new String(encodedHash);
    }
    public static String hashOfMultipartFile(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        byte[] data = file.getBytes();
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(data);
        return bytesToHex(hash);
    }
    public static String hashOfBytes(byte[] file) throws IOException, NoSuchAlgorithmException {
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(file);
        return bytesToHex(hash);
    }
    public static String hashOfFile(File file) throws IOException, NoSuchAlgorithmException {
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(data);
        return bytesToHex(hash);
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
