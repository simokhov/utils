package com.sstd.utils.zip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SstdZipManager {

    // Buffer size for stream
    private static final int BUFFER_SIZE = 4096;

    /**
     * Unpack files without processor
     *
     * @param sourcePath      String path to zip archive
     * @param destinationPath String destination folder
     * @return List of strings with unpacked files path
     * @throws IOException If I/O error occurs
     */
    public static List<String> unpack(String sourcePath, String destinationPath)
            throws IOException {
        return unpack(sourcePath, destinationPath, null);
    }

    /**
     * Unpack files without processor
     * @param sourcePath String path to zip archive
     * @param destinationPath String destination folder
     * @param zipProcessor SstdZipProcessorInterface instance
     * @return List of strings with unpacked files path
     * @throws IOException If I/O error occurs
     */
    public static List<String> unpack(String sourcePath, String destinationPath, SstdZipProcessorInterface zipProcessor)
            throws IOException {
        List<String> result = new ArrayList<>();
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(sourcePath));
        ZipEntry entry = zipInputStream.getNextEntry();
        while (entry != null) {

            String filePath = destinationPath + File.separator + entry.getName();

            // if processor initiated
            if (zipProcessor != null) {
                zipProcessor.beforeUnpack(entry.getName());
                if (zipProcessor.isValid(entry.getName())) {
                    extractFile(zipInputStream, filePath);
                    zipInputStream.closeEntry();
                } else {
                    // if not valid file - get next
                    entry = zipInputStream.getNextEntry();
                    continue;
                }
                zipProcessor.afterUnpack(filePath);
                result.add(filePath);
            } else {
                extractFile(zipInputStream, filePath);
                zipInputStream.closeEntry();
            }
            entry = zipInputStream.getNextEntry();
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                result.add(filePath);
            }
        }
        zipInputStream.close();
        return result;
    }

    /**
     * Extracting file from stream
     *
     * @param zipInputStream ZipInputStream
     * @param filePath String full destination Path
     * @throws IOException ioe
     */
    private static void extractFile(ZipInputStream zipInputStream, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipInputStream.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
