package com.sstd.utils.zip;

public interface SstdZipProcessorInterface {

    /**
     * Validate file for downloading
     * return true if file is valid
     * and must be downloaded
     *
     * @param zipFile String path
     * @return boolean
     */
    boolean isValid(String zipFile);

    /**
     * Method to do some work before unpack file
     *
     * @param zipFile FTPFile instance
     */
    void beforeUnpack(String zipFile);

    /**
     * Method to do some work after unpack file
     *
     * @param zipFile FTPFile instance
     */
    void afterUnpack(String zipFile);

}
