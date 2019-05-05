package com.mirstone.selectfilelib.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @package: com.mirstone.selectfilelib.util
 * @fileName: FileUtil
 * @data: 2018/7/27 15:25
 * @author: ShiLiang
 * @describe:
 */
public final class FileUtil {
    /**
     * 除去隐藏文件夹（.开头文件夹）
     * @return List<File>
     */
    public static List<File> filterHideFile(List<File> files){
        if (files == null || files.isEmpty()) return files;
        ArrayList<File> ret = new ArrayList<>();
        for (File file : files) {
            if (!file.getName().startsWith(".")){
                ret.add(file);
            }
        }
        return ret;
    }
}
