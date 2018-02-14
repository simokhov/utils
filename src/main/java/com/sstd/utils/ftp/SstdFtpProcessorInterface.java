package com.sstd.utils.ftp;

import org.apache.commons.net.ftp.FTPFile;

public interface SstdFtpProcessorInterface {

    /**
     * Validate file for downloading
     * return true if file is valid
     * and must be downloaded
     *
     * @param ftpFile FTPFile instance
     * @return boolean
     */
    boolean isValid(FTPFile ftpFile);

    /**
     * Method to do some work before downloading file
     *
     * @param ftpFile FTPFile instance
     */
    void beforeRetrieve(FTPFile ftpFile);

    /**
     * Method to do some work after downloading file
     *
     * @param ftpFile FTPFile instance
     */
    void afterRetrieve(FTPFile ftpFile);


}
