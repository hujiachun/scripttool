package com.hjc.util;


import java.io.*;
import java.util.zip.*;


public class ZipCompressor {

    /**
     * 使用zip压缩文件
     *
     * @param source 源文件或者文件夹
     */
    public static void zip(File source) {
        String dir = source.getParent();
        File target = new File("/storage/emulated/0/Result/testResult.zip");

        FileOutputStream fos = null;
        ZipOutputStream zipos = null;

        try {
            fos = new FileOutputStream(target);
            zipos = new ZipOutputStream(fos);
            zipFile(source, zipos, "");
            zipos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (zipos != null) {
                    zipos.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 递归压缩文件或者文件夹
     *
     * @param source     源文件
     * @param zipos      zip输出流
     * @param parentPath 路径
     */
    public static void zipFile(File source, ZipOutputStream zipos, String parentPath) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            if (source.isDirectory()) {
                File[] files = source.listFiles();
                if (files.length < 1) {
                    ZipEntry entry = new ZipEntry(parentPath + source.getName() + "/");
                    zipos.putNextEntry(entry);
                }
                for (File file : files) {
                    zipFile(file, zipos, parentPath + source.getName() + "/");
                }
            } else if (source.isFile()) {
                fis = new FileInputStream(source);
                bis = new BufferedInputStream(fis);
                ZipEntry entry = new ZipEntry(parentPath + source.getName());
                zipos.putNextEntry(entry);
                int count;
                byte[] buffer = new byte[1024 * 10];
                while ((count = bis.read(buffer, 0, buffer.length)) != -1) {
                    zipos.write(buffer, 0, count);
                }
                fis.close();
                bis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public ZipCompressor() {
    }

    /**
     * @param srcFiles 需压缩的文件路径及文件名
     * @param desFile  保存的文件名及路径
     *                 <a href="http://my.oschina.net/u/556800" class="referer" target="_blank">@return</a>  如果压缩成功返回true
     */
    public boolean zipCompress(String[] srcFiles, String desFile) {
        boolean isSuccessful = false;

        String[] fileNames = new String[srcFiles.length - 1];
        for (int i = 0; i < srcFiles.length - 1; i++) {
            fileNames[i] = parse(srcFiles[i]);
        }

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(desFile));
            ZipOutputStream zos = new ZipOutputStream(bos);
            String entryName;

            for (int i = 0; i < fileNames.length; i++) {
                entryName = fileNames[i];

                ZipEntry entry = new ZipEntry(entryName);
                zos.putNextEntry(entry);

                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFiles[i]));

                byte[] b = new byte[1024];

                while (bis.read(b, 0, 1024) != -1) {
                    zos.write(b, 0, 1024);
                }
                bis.close();
                zos.closeEntry();
            }

            zos.flush();
            zos.close();
            isSuccessful = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccessful;
    }


    // 解析文件名
    private String parse(String srcFile) {
        int location = srcFile.lastIndexOf("/");
        return srcFile.substring(location + 1);
    }

    private static final int BUFFER = 1024;

    /**
     * 文件 解压缩
     *
     * @param srcPath
     *            源文件路径
     *
     * @throws Exception
     */
    public static void decompress(String srcPath) throws Exception {
        File srcFile = new File(srcPath);
        decompress(srcFile);
    }

    /**
     * 解压缩
     *
     * @param srcFile
     * @throws Exception
     */
    public static void decompress(File srcFile) throws Exception {
        String basePath = srcFile.getParent();
        decompress(srcFile, basePath);
    }

    /**
     * 解压缩
     *
     * @param srcFile
     * @param destFile
     * @throws Exception
     */
    public static void decompress(File srcFile, File destFile) throws Exception {
        CheckedInputStream cis = new CheckedInputStream(new FileInputStream(srcFile), new CRC32());
        ZipInputStream zis = new ZipInputStream(cis);
        decompress(destFile, zis);
        zis.close();
    }

    /**
     * 解压缩
     *
     * @param srcFile
     * @param destPath
     * @throws Exception
     */
    public static void decompress(File srcFile, String destPath) throws Exception {
        decompress(srcFile, new File(destPath));
    }

    /**
     * 文件 解压缩
     *
     * @param srcPath
     *            源文件路径
     * @param destPath
     *            目标文件路径
     * @throws Exception
     */
    public static void decompress(String srcPath, String destPath)
            throws Exception {

        File srcFile = new File(srcPath);
        decompress(srcFile, destPath);
    }

    /**
     * 文件 解压缩
     *
     * @param destFile
     *            目标文件
     * @param zis
     *            ZipInputStream
     * @throws Exception
     */
    private static void decompress(File destFile, ZipInputStream zis)throws Exception {
        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {
            // 文件
            String dir = destFile.getPath() + File.separator + entry.getName();
            File dirFile = new File(dir);
            // 文件检查
            fileProber(dirFile);
            if (entry.isDirectory()) {
                dirFile.mkdirs();
            } else {
                decompressFile(dirFile, zis);
            }
            zis.closeEntry();
        }
    }

    /**
     * 文件探针
     *
     *
     * 当父目录不存在时，创建目录！
     *
     *
     * @param dirFile
     */
    private static void fileProber(File dirFile) {
        File parentFile = dirFile.getParentFile();
        if (!parentFile.exists()) {
            fileProber(parentFile);
            parentFile.mkdir();
        }
    }

    /**
     * 文件解压缩
     *
     * @param destFile
     *            目标文件
     * @param zis
     *            ZipInputStream
     * @throws Exception
     */
    private static void decompressFile(File destFile, ZipInputStream zis)throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = zis.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }
        bos.close();
    }



    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("请输入参数：javac ZipTest 需压缩的文件名及路径 保存的文件名及路径。(e.g. c:/com.hjc.scripttool.txt)");
        } else {
            ZipCompressor compressor = new ZipCompressor();
            boolean isSuccessful = compressor.zipCompress(args, args[args.length - 1]);
            if (isSuccessful) {
                System.out.println("文件压缩成功。");
            } else {
                System.out.println("文件压缩失败。");
            }
        }
    }
}