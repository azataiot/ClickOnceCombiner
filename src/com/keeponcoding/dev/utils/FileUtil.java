package com.keeponcoding.dev.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Yanfei on 2015/7/8.
 */
public class FileUtil
{
    public static boolean deleteDirectory(File directory)
    {
        if (!directory.exists())
        {
            return false;
        }
        File[] childrenFiles = directory.listFiles();
        for (int i = 0; i < childrenFiles.length; i++)
        {
            if (childrenFiles[i].isDirectory())
            {
                deleteDirectory(childrenFiles[i]);
            }
            else
            {
                childrenFiles[i].delete();
            }
        }
        return directory.delete();
    }

    public static boolean removeSuffixInDirectory(File directory, final String suffix)
    {
        if (!directory.exists())
        {
            return false;
        }
        File[] childrenFiles = directory.listFiles();
        for (int i = 0; i < childrenFiles.length; i++)
        {
            File childFile = childrenFiles[i];
            if (childFile.isDirectory())
            {
                removeSuffixInDirectory(childFile, suffix);
            }
            else
            {
                if (childFile.getName().endsWith(suffix))
                {
                    String newName = childFile.getName().replace(suffix, "");
                    String newFilePath = childFile.getParentFile().getAbsolutePath() + "/" + newName;
                    childFile.renameTo(new File(newFilePath));
                }
            }
        }
        return true;
    }

}
