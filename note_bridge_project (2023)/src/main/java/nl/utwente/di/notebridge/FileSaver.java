package nl.utwente.di.notebridge;

import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileSaver {
    private static String saveFile(Part filePart, String realPath, String subdirectory, int pid) {
        String fileName = filePart.getSubmittedFileName();
        File folder = new File(realPath + File.separator + subdirectory);

        // creates folder for files
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return null;
            }
        }

        // make sure the file doesn't contain an extra path
        fileName = fileName.replace("\\", "").replace("/", "");
        fileName = pid + "." + fileName.split("\\.")[fileName.split("\\.").length-1];
        String filePath = File.separator + subdirectory + File.separator + fileName;

        String path = realPath + filePath;
        //copy the file to the path
        try (InputStream inputStream = filePart.getInputStream();
             OutputStream outputStream = new FileOutputStream(path)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            //return the path of the file
            return File.separator + "uploads" + filePath;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String saveFile(InputStream fileInputStream, String realPath, String name, String subdirectory, int pid) {
        File folder = new File(realPath + File.separator + subdirectory);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return null;
            }
        }

        String fileName = name.replace("\\", "").replace("/", "");
        fileName = pid + "." + fileName.split("\\.")[fileName.split("\\.").length-1];

        try {
            Files.copy(fileInputStream, Paths.get(realPath + File.separator + subdirectory + File.separator + fileName), StandardCopyOption.REPLACE_EXISTING);
            return File.separator + "uploads" + File.separator + subdirectory + File.separator + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveProfilePicture(Part filePart, String realPath, int pid) {
        return saveFile(filePart, realPath, "profile-pictures", pid);
    }

    public static String saveVideo(Part filePart, String realPath, int pid) {
        return saveFile(filePart, realPath, "videos", pid);
    }

    public static String saveProfilePicture(InputStream fileInputStream, String realPath, String fileName, int pid) {
        return saveFile(fileInputStream, realPath, fileName, "profile-pictures", pid);
    }

    public static String saveVideo(InputStream fileInputStream, String realPath, String fileName, int pid) {
        return saveFile(fileInputStream, realPath, fileName, "videos", pid);
    }
}
