package com.keeponcoding.dev.clickoncecombiner.models;

/**
 * Created by Yanfei on 2015/7/7.
 */
public class Dependency
{
    private String relativeUrl;
    private String relativeSaveDir;

    public String getRelativeUrl()
    {
        return relativeUrl;
    }

    public void setRelativeUrl(String relativeUrl)
    {
        this.relativeUrl = relativeUrl;
    }

    public String getRelativeSaveDir()
    {
        return relativeSaveDir;
    }

    public void setRelativeSaveDir(String relativeSaveDir)
    {
        this.relativeSaveDir = relativeSaveDir;
    }
}
