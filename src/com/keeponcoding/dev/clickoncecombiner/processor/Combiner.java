package com.keeponcoding.dev.clickoncecombiner.processor;

import com.keeponcoding.dev.clickoncecombiner.models.Dependency;
import com.keeponcoding.dev.core.HttpClient;
import com.keeponcoding.dev.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Yanfei on 2015/7/8.
 */
public class Combiner
{

    private static final String APPEDN_STRING = ".deploy";

    public void installApp(String url, String installDir) throws IOException
    {
        //0.Remove CacheDir
        FileUtil.deleteDirectory(new File(installDir));
        //1.Combine
        boolean combineSuccess = doCombine(url, installDir);
        if (!combineSuccess)
        {
            return;
        }
        //2.Remove Append Suffix
        FileUtil.removeSuffixInDirectory(new File(installDir), APPEDN_STRING);
        //3.Done
        System.out.println("Done.");
        String command = "explorer " + installDir;
        Runtime.getRuntime().exec(command);
    }

    public boolean doCombine(String url, String saveDir)
    {
        //1.Download file (xml)
        HttpClient httpClient = new HttpClient();
        HttpClient.FileInfo fileInfo = httpClient.downloadFile(url, saveDir);
        if (fileInfo == null)
        {
            System.out.println("Can't reach the url: " + url + ", please try it later.");
            return false;
        }
        else
        {
            System.out.println("Success download from the url: " + url);
        }

        //2.Parse
        boolean needParse = checkNeedParse(fileInfo.getContentType());
        boolean needAppend = checkNeedAppend(fileInfo.getFileName());
        if (needParse)
        {
            String baseDir = fileInfo.getSaveDir();
            String baseUrl = fileInfo.getBaseUrl();
            String fileName = fileInfo.getFileName();

            XmlParser parser = new XmlParser();
            String filePath = baseDir + File.separator + fileName;
            parser.parse(filePath);

            List<Dependency> dependencies = parser.getDependencies();
            for (Dependency dependency : dependencies)
            {
                String dependencyDownloadUrl = baseUrl + "/" + dependency.getRelativeUrl();
                if (needAppend)
                {
                    dependencyDownloadUrl += APPEDN_STRING;
                }
                String dependencySaveDir = baseDir + "/" + dependency.getRelativeSaveDir();
                doCombine(dependencyDownloadUrl, dependencySaveDir);
            }
        }
        return true;
    }

    private boolean checkNeedParse(String contentType)
    {
        return contentType.contains("application/x-ms");
    }

    private boolean checkNeedAppend(String fileName)
    {
        return fileName.contains(".manifest");
    }
}
