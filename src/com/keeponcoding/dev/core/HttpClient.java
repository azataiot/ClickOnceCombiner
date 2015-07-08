package com.keeponcoding.dev.core;

import com.keeponcoding.dev.utils.FileUtil;
import com.sun.deploy.net.HttpResponse;

import java.io.*;
import java.net.*;

/**
 * Created by Yanfei on 2015/7/7.
 */
public class HttpClient
{
    enum HttpType
    {
        POST, GET
    }

    public class FileInfo
    {
        private String baseUrl;
        private String saveDir;
        private String fileName;
        private String contentType;
        private int contentLength;

        public String getSaveDir()
        {
            return saveDir;
        }

        public void setSaveDir(String saveDir)
        {
            this.saveDir = saveDir;
        }

        public String getBaseUrl()
        {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl)
        {
            this.baseUrl = baseUrl;
        }

        public String getFileName()
        {
            return fileName;
        }

        public void setFileName(String fileName)
        {
            this.fileName = fileName;
        }

        public String getContentType()
        {
            return contentType;
        }

        public void setContentType(String contentType)
        {
            this.contentType = contentType;
        }

        public int getContentLength()
        {
            return contentLength;
        }

        public void setContentLength(int contentLength)
        {
            this.contentLength = contentLength;
        }
    }

    public String syncReq(String url, String params, HttpType method)
    {
        if (method == HttpType.POST)
        {
            return syncPostReq(url, params);
        }
        else
        {
            return syncGetReq(url, params);
        }
    }

    public String syncPostReq(String url, String params)
    {
        StringBuilder result = new StringBuilder("");
        try
        {
            URL reqUrl = new URL(url);
            URLConnection connection = reqUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            writer.write(params);
            writer.flush();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                result.append(line);
            }

            bufferedReader.close();
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String syncGetReq(String url, String params)
    {
        StringBuilder result = new StringBuilder("");
        try
        {
            URL reqUrl = new URL(url + "?" + params);
            URLConnection connection = reqUrl.openConnection();

            connection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                result.append(line);
            }
            bufferedReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * Download File from the url and save to the saveDir
     *
     * @param fileUrl
     * @param saveDir
     * @return file save path
     */
    public FileInfo downloadFile(String fileUrl, String saveDir)
    {
        FileInfo fileInfo = null;
        String fileName = "";
        try
        {
            URL url = new URL(fileUrl.replace(" ", "%20"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 6.3; WOW64; rv:38.0)");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                String disposition = connection.getHeaderField("Content-Disposition");
                if (disposition != null)
                {
                    int index = disposition.indexOf("filename=");
                    if (index > 0)
                    {
                        fileName = disposition.substring(index + 10, disposition.length() - 1);
                    }
                }
                else
                {
                    fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());
                }

                File file = new File(saveDir);
                if (!file.exists())
                {
                    file.mkdirs();
                }

                String saveFilePath = saveDir + "/" + fileName;

                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, len);
                }
                inputStream.close();
                outputStream.close();

                String contentType = connection.getContentType();
                int contentLength = connection.getContentLength();

                fileInfo = new FileInfo();
                fileInfo.setBaseUrl(fileUrl.substring(0, fileUrl.lastIndexOf("/")));
                fileInfo.setSaveDir(saveDir);
                fileInfo.setContentType(contentType);
                fileInfo.setFileName(fileName);
                fileInfo.setContentLength(contentLength);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return fileInfo;
    }
}
