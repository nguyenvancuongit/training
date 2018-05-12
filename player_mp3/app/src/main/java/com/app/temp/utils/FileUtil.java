package com.app.temp.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Windows 7 on 1/4/2016.
 */
public class FileUtil {
    /**
     * download a file from server
     *
     * @param urlLink  ex https://pbs.twimg.com/profile_images/616076655547682816
     * @param fileName ex 6gMRtQyY.jpg
     * @param pathSave ex sdcard0/data/save_path
     * @return true if successful
     */
    public static boolean doDownload(final String urlLink, final String fileName, String pathSave) {
        Boolean check = false;

        File dir = new File(pathSave);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            URL url = new URL(urlLink);
            URLConnection connection = url.openConnection();
            connection.connect();
            connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(dir + "/" + fileName);

            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            check = true;
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    /**
     * unzip a ZIP file from local store
     *
     * @param path    ex sdcard/data
     * @param zipname ex zz.zip
     * @return true if successful
     */
    public static boolean unpackZip(String path, String zipname) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * getNameFromUrl
     */
    public static String getNameFromUrl(String urlLink) {
        String[] parts = urlLink.split("\\/");
        return parts[parts.length - 1];
    }

    /**
     * scan all file name from a directory
     *
     * @param path ex sdcard/data
     * @return list file path
     */
    public static Vector<String> scanListFileOnDirectory(String path) {
        Vector<String> listNameFileSaved = new Vector<>();
        File wallpaperDirectory = new File(path);
        if (!wallpaperDirectory.exists())
            wallpaperDirectory.mkdir();
        for (File f : wallpaperDirectory.listFiles()) {
            if (f.isFile()) {
                listNameFileSaved.add(f.getName());
            }
        }
        return listNameFileSaved;
    }

    /**
     * delete all file from a directory
     *
     * @param path ex sdcard/data
     */
    public static void deleteFiles(String path) {
        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
