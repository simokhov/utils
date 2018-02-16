package com.sstd.utils;

public interface SstdProcessorInterface {

    /**
     * Validate file for processing
     * return true if file is valid
     *
     * @param filePath String path
     * @return boolean
     */
    boolean isValid(String filePath);

    /**
     * Method to do some work before process file
     *
     * @param filePath String path
     */
    void before(String filePath);

    /**
     * Method to do some work after process file
     *
     * @param filePath String path
     */
    void after(String filePath);

}
