package com.sstd.utils.xml;

public interface SstdXmlParserInterface<T> {

    void parse(T object);

    Class<T> getResourceClass();
}
