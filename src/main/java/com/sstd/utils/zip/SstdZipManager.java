package com.sstd.utils.zip;

import com.sstd.utils.SstdProcessorInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SstdZipManager {

    // Buffer size for stream
    private static final int BUFFER_SIZE = 4096;
    private SstdProcessorInterface zipProcessor;
    private ExecutorService executorService;

    public SstdZipManager() {
    }

    public SstdZipManager(SstdProcessorInterface zipProcessorInterface) {
        this.zipProcessor = zipProcessorInterface;
    }

    public SstdZipManager(SstdProcessorInterface zipProcessor, ExecutorService executorService) {
        this.zipProcessor = zipProcessor;
        this.executorService = executorService;
    }

    /**
     * Unpack files without processor
     * @param sourcePath String path to zip archive
     * @param destinationPath String destination folder
     * @return List of strings with unpacked files path
     * @throws IOException If I/O error occurs
     */
    private List<String> unpack(String sourcePath, String destinationPath)
            throws IOException {
        List<String> result = new ArrayList<>();
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(sourcePath));
        ZipEntry entry = zipInputStream.getNextEntry();
        while (entry != null) {

            String filePath = destinationPath + File.separator + entry.getName();

            // if processor initiated
            if (zipProcessor != null) {
                zipProcessor.before(entry.getName());
                if (zipProcessor.isValid(entry.getName())) {
                    extractFile(zipInputStream, filePath);
                    zipInputStream.closeEntry();
                } else {
                    // if not valid file - get next
                    entry = zipInputStream.getNextEntry();
                    continue;
                }
                zipProcessor.after(filePath);
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

    /**
     * Sync unpack zip file to destination
     *
     * @param sourcePath      String path to zip
     * @param destinationPath String path to destination derectory
     * @return List of unpacked file's paths
     * @throws IOException if I\O error occurs
     */
    public List<String> syncTask(String sourcePath, String destinationPath) throws IOException {
        return unpack(sourcePath, destinationPath);
    }

    /**
     * ASync unpack zip file to destination
     *
     * @param sourcePath      String path to zip
     * @param destinationPath String path to destination derectory
     * @return Future List of unpacked file's paths
     */
    public Future<List<String>> asyncTask(String sourcePath, String destinationPath) {
        return executorService.submit(new ZipTaskThread(this, sourcePath, destinationPath));
    }

    private class ZipTaskThread implements Callable<List<String>> {

        private SstdZipManager zipManager;
        private String sourcePath;
        private String destinationPath;

        ZipTaskThread(SstdZipManager zipManager, String sourcePath, String destinationPath) {
            this.zipManager = zipManager;
            this.sourcePath = sourcePath;
            this.destinationPath = destinationPath;
        }

        @Override
        public List<String> call() {
            List<String> result = new ArrayList<>();
            try {
                result.addAll(zipManager.unpack(sourcePath, destinationPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
