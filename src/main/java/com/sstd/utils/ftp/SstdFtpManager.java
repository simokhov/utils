package com.sstd.utils.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SstdFtpManager {

    private SstdFtpConnection sstdFtpConnection;

    public SstdFtpManager(SstdFtpConnection sstdFtpConnection) {
        this.sstdFtpConnection = sstdFtpConnection;
    }

    private FTPClient getClient() {
        return sstdFtpConnection.getFtpClient();
    }

    private boolean retrieveFile(FTPFile ftpFile, String localPath) throws IOException {
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localPath));
        return getClient().retrieveFile(ftpFile.getLink(), outputStream);
    }
}
