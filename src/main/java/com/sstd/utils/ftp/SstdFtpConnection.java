package com.sstd.utils.ftp;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class SstdFtpConnection {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private FTPClient ftpClient;

    public SstdFtpConnection(String host, Integer port, String username, String password) throws IOException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        ftpClient = initFtpClient();
    }

    private FTPClient initFtpClient() throws IOException {
        FTPClient ftpClientInstance = new FTPClient();

//        Server connection
        if (port != null) {
            ftpClientInstance.connect(host, port);
        } else {
            ftpClientInstance.connect(host);
        }

//        Authorization
        if (!username.isEmpty() && !password.isEmpty()) {
            ftpClientInstance.login(username, password);
        }

//        Entering Passive Transfer Mode
        ftpClientInstance.enterLocalPassiveMode();

        ftpClientInstance.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClientInstance.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);

        return ftpClientInstance;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }
}
