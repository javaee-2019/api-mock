package com.gameloft9.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class FileUtil {

    /**
     * 复制文件
     *
     * @param source 源
     * @param target 目标
     * @throws IOException ioexception
     */
    public static void copyFile(File source, File target) throws IOException {
        if (!(source.isFile())) {
            throw new IOException(String.format("[%s] is not file", source.getAbsolutePath()));
        }
        FileCopyUtils.copy(source, target);
    }


    /**
     * 输入stream2文件副本
     *
     * @param source 源
     * @param target 目标
     * @return boolean
     */
    public static boolean copyInputStream2File(InputStream source, File target) {
        try {
            FileCopyUtils.copy(source, new FileOutputStream(target));
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


    /**
     * dir副本
     *
     * @param source 源
     * @param target 目标
     * @return boolean
     * @throws IOException ioexception
     */
    public static boolean copyDir(File source, File target) throws IOException {
        if (Objects.isNull(source) || !source.exists()) {
            return false;
        }
        if (!target.exists()) {
            target.mkdirs();
        }
        String sourceAbsolutePath = source.getAbsolutePath();
        String targetAbsolutePath = target.getAbsolutePath();

        for (File file : listFiles(source)) {
            if (!file.exists()) {
                continue;
            }
            File targetFile = new File(file.getAbsolutePath().replace(sourceAbsolutePath, targetAbsolutePath));
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            FileCopyUtils.copy(file, targetFile);
        }
        return true;
    }


    /**
     * 删除
     *
     * @param deleteFile 删除文件
     * @return boolean
     */
    public static boolean delete(File deleteFile) {
        if (!deleteFile.exists()) {
            return true;
        }
        for (File file : listFilesWithDir(deleteFile)) {
            if (!file.exists()) {
                continue;
            }
            boolean success = file.delete();
            if (!success) {
                return false;
            }
        }
        // 目录此时为空，可以删除
        return true;
    }


    /**
     * 删除
     *
     * @param dir dir
     * @return boolean
     */
    public static boolean delete(String dir) {
        return delete(new File(dir));
    }


    /**
     * 列表文件
     *
     * @param file 文件
     * @return {@link List<File>}
     */
    public static List<File> listFiles(File file) {
        return listFiles(file, new ArrayList<>());
    }


    /**
     * 文件列表包含文件夹路径
     *
     * @param file 文件
     * @return {@link List<File>}
     */
    public static List<File> listFilesWithDir(File file) {
        return listFilesWithDir(file, new ArrayList<>());
    }


    /**
     * 列表文件
     *
     * @param file     文件
     * @param fileList 文件列表
     * @return {@link List<File>}
     */
    private static List<File> listFiles(File file, List<File> fileList) {
        if (file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                listFiles(childFile, fileList);
            }
        } else {
            fileList.add(file);
        }
        return fileList;
    }


    /**
     * 文件列表包含文件夹路径
     *
     * @param file     文件
     * @param fileList 文件列表
     * @return {@link List<File>}
     */
    private static List<File> listFilesWithDir(File file, List<File> fileList) {
        if (file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                listFilesWithDir(childFile, fileList);
            }
        }
        fileList.add(file);
        return fileList;
    }

    /**
     * 逐行读取文本文件
     *
     * @param file 文件
     * @return {@link List<String>}
     * @throws IOException ioexception
     */
    public static List<String> readTxtLineList(File file) throws IOException {
        if (Objects.isNull(file) && !file.exists()) {
            throw new IOException("file not find");
        }
        List<String> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        } finally {

        }
        return list;
    }

    /**
     * 逐行写数据
     */
    public static boolean writeTxtLineList(List<String> strList, File targetFile) {
        if (Objects.nonNull(targetFile) && targetFile.exists()) {
            targetFile.delete();
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetFile))) {
            for (String str : strList) {
                bufferedWriter.write(str);
                bufferedWriter.newLine();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {

        }
        return true;
    }
}
