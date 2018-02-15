package com.sstd.utils.zip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SstdZipManager {

    // Buffer size for stream
    private static final int BUFFER_SIZE = 4096;

    private SstdZipProcessorInterface zipProcessor;

    public SstdZipManager() {
    }

    public SstdZipManager(SstdZipProcessorInterface zipProcessor) {
        this.zipProcessor = zipProcessor;
    }

    public List<String> unpack(String sourcePath, String destinationPath) throws IOException {
        List<String> result = new ArrayList<>();
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(sourcePath));
        ZipEntry entry = zipInputStream.getNextEntry();
        while (entry != null) {

            // if processor set and not valid entry - skipping
            if (zipProcessor != null && !zipProcessor.isValid(entry.getName())) {
                continue;
            }

            String filePath = destinationPath + File.separator + entry.getName();
            extractFile(zipInputStream, filePath);
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
     * @param zipIn    ZipInputStream
     * @param filePath String full destination Path
     * @throws IOException ioe
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
