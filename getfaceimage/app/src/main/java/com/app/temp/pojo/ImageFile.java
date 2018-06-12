package com.app.temp.pojo;

import java.io.File;

public class ImageFile {

    private File file;
    private boolean isContainFace = false;

    public ImageFile(File file, boolean isContainFace) {
        this.file = file;
        this.isContainFace = isContainFace;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isContainFace() {
        return isContainFace;
    }

    public void setContainFace(boolean containFace) {
        isContainFace = containFace;
    }
}
