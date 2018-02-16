package com.sstd.utils.xml;

import com.sstd.utils.SstdProcessorInterface;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class SstdXmlManager {

    private SstdProcessorInterface xmlProcessor;
    private Class[] classes;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    public SstdXmlManager(Class... classes) throws JAXBException {
        this.classes = classes;
        init();
    }

    public SstdXmlManager(SstdProcessorInterface xmlProcessor, Class... classes) throws JAXBException {
        this.xmlProcessor = xmlProcessor;
        this.classes = classes;
        init();
    }

    public Marshaller getMarshaller() {
        return marshaller;
    }

    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

    /**
     * Initiate JAXB with context
     *
     * @throws JAXBException Error while create content
     */
    private void init() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(classes);
        unmarshaller = jaxbContext.createUnmarshaller();
        marshaller = jaxbContext.createMarshaller();
    }

    /**
     * Transform to Object ned be casted
     *
     * @param xml File instance
     * @return Object
     * @throws JAXBException Unmarshaller error
     */
    private Object transform(File xml) throws JAXBException {
        return unmarshaller.unmarshal(xml);
    }

    /**
     * Transform to Object ned be casted
     *
     * @param path String path
     * @return Object
     * @throws JAXBException Unmarshaller error
     */
    public Object transform(String path) throws JAXBException {
        File file = new File(path);
        return transform(file);
    }

    public void process(File xml, SstdXmlParserInterface<Object> xmlParser) throws JAXBException {
        Object object = transform(xml);

        String absoluteFilePath = xml.getAbsolutePath();

        if (xmlProcessor != null) {
            xmlProcessor.before(absoluteFilePath);
            if (xmlProcessor.isValid(absoluteFilePath)) {
                xmlParser.parse(object);
            }
            xmlProcessor.after(absoluteFilePath);
        } else {
            xmlParser.parse(object);
        }

    }
}
