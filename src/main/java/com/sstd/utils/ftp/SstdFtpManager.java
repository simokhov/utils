package com.sstd.utils.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * SstdFtpManager class
 * Used to retrieve files from FTP
 * You must inject SstdFtpConnection on construct
 */
public class SstdFtpManager {

    private SstdFtpConnection sstdFtpConnection;

    public SstdFtpManager(SstdFtpConnection sstdFtpConnection) {
        this.sstdFtpConnection = sstdFtpConnection;
    }

    private FTPClient getClient() {
        return sstdFtpConnection.getFtpClient();
    }

    /**
     * @param ftpFile   FTPFile
     * @param localPath String
     *                  Path to store local file
     *                  Can Throw FileNotFoundException
     * @return boolean
     * @throws IOException Throws if local FileNotFoundException or
     *                     an I/O error occurs while either sending a
     *                     command to the server or receiving a reply from the server.
     */
    private boolean retrieveFile(FTPFile ftpFile, String localPath) throws IOException {
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localPath));
        return getClient().retrieveFile(ftpFile.getLink(), outputStream);
    }
}
