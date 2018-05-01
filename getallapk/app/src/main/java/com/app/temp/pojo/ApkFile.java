package com.app.temp.pojo;

import java.io.File;

public class ApkFile {

    private File file;
    private boolean isInstalled = false;

    public ApkFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }
}
