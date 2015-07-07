package com.keeponcoding.dev.clickoncecombiner;

import com.keeponcoding.dev.clickoncecombiner.models.Dependency;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yanfei on 2015/7/7.
 */
public class XmlParser extends DefaultHandler implements ErrorHandler
{

    private List<Dependency> dependencies = new ArrayList<>();

    public List<Dependency> getDependencies()
    {
        return dependencies;
    }

    public void parse(String xml)
    {
        try
        {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(this);
            xmlReader.setErrorHandler(this);
            xmlReader.parse(new InputSource(new ByteArrayInputStream(xml.getBytes())));
        }
        catch (SAXException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void startDocument() throws SAXException
    {
        super.startDocument();
//        System.out.println("Start Parse Document.");
    }

    @Override
    public void endDocument() throws SAXException
    {
        super.endDocument();
//        System.out.println("End Parse Document.");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        super.startElement(uri, localName, qName, attributes);
//        System.out.println("Begin Parse Element");
//        System.out.println("Element - qName: " + qName);
//        System.out.println("Element - localName: " + localName);
//        System.out.println("Element - uri: " + uri);
//        for (int i = 0; i < attributes.getLength(); i++)
//        {
//            System.out.println("");
//            System.out.println(" attribute qName: " + attributes.getQName(i));
//            System.out.println(" attribute localName: " + attributes.getLocalName(i));
//            System.out.println(" attribute uri: " + attributes.getURI(i));
//            System.out.println(" attribute value: " + attributes.getValue(i));
//        }
        if (qName.equals("dependentAssembly"))
        {
            String dependencyType = attributes.getValue("dependencyType");
            if (dependencyType.equals("install"))
            {
                String codebase = attributes.getValue("codebase");
                if (codebase != null && codebase.length() > 0 )
                {
                    Dependency dependency = new Dependency();
                    dependency.setSavePath(codebase.replace("\\", "/"));
                    Path path = Paths.get(codebase);
                    dependency.setFileName(path.getFileName().toString());
                    dependencies.add(dependency);
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        super.endElement(uri, localName, qName);
//        System.out.println("Begin Parse Element");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        super.characters(ch, start, length);
//        System.out.println("Parse Content is :" + new String(ch));
//        System.out.println("Parse Content start index: " + start);
//        System.out.println("Parse Content lenght: " + length);
    }
}
