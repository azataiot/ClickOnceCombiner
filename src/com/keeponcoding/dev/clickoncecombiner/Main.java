package com.keeponcoding.dev.clickoncecombiner;

import com.keeponcoding.dev.clickoncecombiner.processor.Combiner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Yanfei on 2015/7/7.
 */
public class Main
{
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Please input your insall direcory path: ");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String installDir = bufferedReader.readLine();
            System.out.println("Install begin...");
            Combiner combiner = new Combiner();
            combiner.installApp("http://github-windows.s3.amazonaws.com/GitHub.application", installDir);
            bufferedReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Program crashed. ");
        }
    }
}
