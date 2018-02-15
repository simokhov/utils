package com.sstd.utils.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SstdFtpManager class
 * Used to retrieve files from FTP
 * You must inject SstdFtpConnection on construct
 */
public class SstdFtpManager {

    private SstdFtpConnection sstdFtpConnection;
    private SstdFtpProcessorInterface sstdFtpProcessor;

    public SstdFtpManager(SstdFtpConnection sstdFtpConnection) {
        this.sstdFtpConnection = sstdFtpConnection;
    }

    public SstdFtpManager(SstdFtpConnection sstdFtpConnection, SstdFtpProcessorInterface sstdFtpProcessor) {
        this.sstdFtpConnection = sstdFtpConnection;
        this.sstdFtpProcessor = sstdFtpProcessor;
    }

    private FTPClient getClient() {
        return sstdFtpConnection.getFtpClient();
    }

    /**
     * @param ftpFile   FTPFile
     * @param localPath String
     *                  Path to store local file
     *                  Can Throw FileNotFoundException
     * @return List of downloaded file's local paths
     * @throws IOException Throws if local FileNotFoundException or
     *                     an I/O error occurs while either sending a
     *                     command to the server or receiving a reply from the server.
     */
    private boolean retrieveFile(String ftpFile, String localPath) throws IOException {
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localPath));
        boolean result = getClient().retrieveFile(ftpFile, outputStream);
        outputStream.close();
        return result;
    }

    public List<String> getRemoteFileList(List<String> fileList, File destinationFolder) throws IOException {
        List<String> result = new ArrayList<>();

        if (!destinationFolder.exists() ||
                !destinationFolder.isDirectory() ||
                !destinationFolder.canWrite()
                ) {
            throw new IOException("Destination folder does not exists or not is directory or no write permission");
        }

        for (String file : fileList) {
            FTPFile ftpFile = getFtpFileInfoByRemotePath(file);
            String destination = destinationFolder.getAbsolutePath() + File.separator + ftpFile.getName();
            getRemoteFile(file, destination);
            File localFile = new File(destination);
            if (localFile.exists()) {
                result.add(destination);
            }
        }

        return result;
    }


    /**
     * @param remotePath String
     *                   Path to remote file
     * @param localPath String
     *                  Path to store local file
     *                  Can Throw FileNotFoundException
     * @throws IOException Throws if local FileNotFoundException or
     *                     an I/O error occurs while either sending a
     *                     command to the server or receiving a reply from the server.
     */
    private void getRemoteFile(String remotePath, String localPath) throws IOException {


        if (sstdFtpProcessor != null) {

            //        Before downloading stuff
            sstdFtpProcessor.beforeRetrieve(remotePath);

            // Validate file and retrieve from remote host
            if (sstdFtpProcessor.isValid(remotePath)) {
                retrieveFile(remotePath, localPath);
            }

            //        After downloading stuff
            sstdFtpProcessor.afterRetrieve(remotePath);
        } else {
            retrieveFile(remotePath, localPath);
        }

    }

    /**
     *
     * @param remotePath String
     * @param recursive boolean flag
     * @return List of strings. Contains full relative paths
     * @throws IOException if I/O error occurs
     */
    public List<String> getValidFileList(String remotePath, boolean recursive) throws IOException {
        List<String> files = new ArrayList<>();
        FTPFile[] ftpFiles = getClient().listFiles(remotePath);
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.isFile()) {
                files.add(remotePath);
            } else if (ftpFile.isDirectory() && recursive) {
                files.addAll(getValidFileList(remotePath + File.separator + ftpFile.getName(), true));
            }
        }
        return files;
    }

    /**
     * Get instance of FTPFile by path
     * @param remotePath String
     * @return FTPFile|null
     * @throws IOException if I/O error occurs
     */
    public FTPFile getFtpFileInfoByRemotePath(String remotePath) throws IOException {
        FTPFile[] ftpFiles = getClient().listFiles(remotePath);
        if (ftpFiles.length == 1) return ftpFiles[0];
        else return null;
    }

}
