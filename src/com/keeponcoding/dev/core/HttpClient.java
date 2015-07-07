package com.keeponcoding.dev.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Yanfei on 2015/7/7.
 */
public class HttpClient
{
    enum HttpType
    {
        POST, GET
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

}
