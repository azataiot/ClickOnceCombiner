package com.keeponcoding.dev.clickoncecombiner.processor;

import com.keeponcoding.dev.clickoncecombiner.models.Dependency;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
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

    public void parse(String filePath)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(filePath);
            parse(inputStream);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void parseContent(String xml)
    {
        parse(new ByteArrayInputStream(xml.getBytes()));
    }

    public void parse(InputStream inputStream)
    {
        try
        {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(this);
            xmlReader.setErrorHandler(this);
            xmlReader.parse(new InputSource(inputStream));
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
    }

    @Override
    public void endDocument() throws SAXException
    {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("dependentAssembly"))
        {
            String dependencyType = attributes.getValue("dependencyType");
            if (dependencyType.equals("install"))
            {
                String codebase = attributes.getValue("codebase");
                if (codebase != null && codebase.length() > 0 )
                {
                    Dependency dependency = new Dependency();
                    if (codebase.contains("\\"))
                    {
                        String tempPath = codebase.replace("\\", "/");
                        dependency.setRelativeUrl(tempPath);
                        dependency.setRelativeSaveDir(tempPath.substring(0, tempPath.lastIndexOf("/")));
                    }
                    else
                    {
                        dependency.setRelativeUrl(codebase);
                        dependency.setRelativeSaveDir("");
                    }
                    dependencies.add(dependency);
                }
            }
        }
        if (qName.equals("file"))
        {
            String fileName = attributes.getValue("name");
            if (fileName != null && fileName.length() > 0 )
            {
                Dependency dependency = new Dependency();
                if (fileName.contains("\\"))
                {
                    String tempPath = fileName.replace("\\", "/");
                    dependency.setRelativeUrl(tempPath);
                    dependency.setRelativeSaveDir(tempPath.substring(0, tempPath.lastIndexOf("/")));
                }
                else
                {
                    dependency.setRelativeUrl(fileName);
                    dependency.setRelativeSaveDir("");
                }
                dependencies.add(dependency);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        super.characters(ch, start, length);
    }
}
