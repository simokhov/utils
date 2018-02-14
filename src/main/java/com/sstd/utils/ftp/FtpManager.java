package com.sstd.utils.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FtpManager {

    private Connection connection;

    public FtpManager(Connection connection) {
        this.connection = connection;
    }

    private FTPClient getClient() {
        return connection.getFtpClient();
    }

    private boolean retrieveFile(FTPFile ftpFile, String localPath) throws IOException {
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localPath));
        return getClient().retrieveFile(ftpFile.getLink(), outputStream);
    }
}
