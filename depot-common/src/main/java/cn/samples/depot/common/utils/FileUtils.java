package cn.samples.depot.common.utils;

import java.io.*;

/**
 * @author ZhangPeng
 * @Title: FileUtil
 * @Copyright: Copyright (c) 2016
 * @Description:
 * @Created 2019-08-12
 */
public class FileUtils {
    // 生成文件路径
    // private static String path = "D:\\file\\";

    // 文件路径+名称
    private static String filenameTemp;

    /**
     * 创建文件
     *
     * @param fileName 文件名称
     * @return 是否创建成功，成功则返回true
     */
    public static boolean createFile(String filePath, String fileName) {
        Boolean bool = false;
        // 文件路径+名称+文件类型
        filenameTemp = filePath + fileName;
        File file = new File(filenameTemp);
        try {
            // 如果文件不存在，则创建新的文件
            if (!file.exists()) {
                file.createNewFile();
                bool = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 创建文件
     *
     * @param fileName    文件名称
     * @param filecontent 文件内容
     * @return 是否创建成功，成功则返回true
     */
    public static boolean writeContent(String filePath, String fileName, String filecontent) {
        Boolean bool = false;
        filenameTemp = filePath + fileName;// 文件路径+名称
        File file = new File(filenameTemp);
        try {
            // 如果文件不存在，则创建新的文件
            if (!file.exists()) {
                file.createNewFile();
                bool = true;
            }
            // 写入内容到文件里
            writeFileContent(filenameTemp, filecontent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 向文件中写入内容
     *
     * @param filepath 文件路径与名称
     * @param newstr   写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath, String newstr) throws IOException {
        Boolean bool = false;
        String filein = newstr + "\r\n";// 新写入的行，换行
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);// 文件路径(包括文件名称)
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            // 文件原有内容
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            // 不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean delFile(String filePath, String fileName) {
        Boolean bool = false;
        filenameTemp = filePath + fileName;
        File file = new File(filenameTemp);
        try {
            if (file.exists()) {
                file.delete();
                bool = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 删除文件夹及其下面的子文件夹
     *
     * @param dir
     * @throws IOException
     */
    public static void deleteDir(File dir) throws IOException {
        if (dir.isFile())
            throw new IOException("IOException -> BadInputException: not a directory.");
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    file.delete();
                } else {
                    deleteDir(file);
                }
            }
        }
        dir.delete();
    }

    /**
     * 将文件名中的类型部分去掉。
     *
     * @param filename
     * @return
     */
    public static String removeFileExt(String filename) {
        int index = filename.lastIndexOf(".");
        if (index != -1) {
            return filename.substring(0, index);
        } else {
            return filename;
        }
    }
}
