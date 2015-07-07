package com.keeponcoding.dev.clickoncecombiner;

import com.keeponcoding.dev.clickoncecombiner.models.Dependency;
import com.keeponcoding.dev.core.HttpClient;

import java.util.List;

/**
 * Created by Yanfei on 2015/7/7.
 */
public class Main
{
    public static void main(String[] args)
    {
        String installPath = "C:/Users/Yanfei/Downloads/TestFolder";
        String baseUrl = "http://github-windows.s3.amazonaws.com/";
        String clickOnceApplicatonName = "GitHub.application";

        //1.Download .application File (xml)
        HttpClient httpClient = new HttpClient();
        String applicationXml = httpClient.syncGetReq(baseUrl + clickOnceApplicatonName , "");
        if (applicationXml == null)
        {
            System.out.println("application xml file  download error, please try again later.");
            return;
        }
        System.out.println("application Xml: " + applicationXml);

        //2.Parse the application xml
        XmlParser parser = new XmlParser();
        parser.parse(applicationXml);
        List<Dependency> dependencies = parser.getDependencies();
        for (Dependency dependency : dependencies)
        {
            System.out.println("Dependency fileName: " + dependency.getFileName());
            System.out.println("Dependency savePath: " + dependency.getSavePath());
            System.out.println("Dependency download url :" + baseUrl + dependency.getSavePath());
        }

        //3.Download the Dependency File
        Downloader downloader = new Downloader();
        for (Dependency dependency : dependencies)
        {
            downloader.DownloadFile(dependency.getSavePath(), baseUrl + dependency.getSavePath());
        }
    }
}
