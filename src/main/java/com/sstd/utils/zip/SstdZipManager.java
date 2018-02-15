package com.sstd.utils.zip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SstdZipManager {

    // Buffer size for stream
    private static final int BUFFER_SIZE = 4096;

    public static List<String> unpack(String sourcePath, String destinationPath)
            throws IOException {
        return unpack(sourcePath, destinationPath, null);
    }

    public static List<String> unpack(String sourcePath, String destinationPath, SstdZipProcessorInterface zipProcessor)
            throws IOException {
        List<String> result = new ArrayList<>();
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(sourcePath));
        ZipEntry entry = zipInputStream.getNextEntry();
        while (entry != null) {

            String filePath = destinationPath + File.separator + entry.getName();

            if (zipProcessor != null) {
                zipProcessor.beforeUnpack(entry.getName());
                if (zipProcessor.isValid(entry.getName())) {
                    extractFile(zipInputStream, filePath);
                } else {
                    // if not valid file - get next
                    continue;
                }

                zipProcessor.afterUnpack(filePath);
                result.add(filePath);

            } else {
                extractFile(zipInputStream, filePath);
            }

            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                result.add(filePath);
            }
        }
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
