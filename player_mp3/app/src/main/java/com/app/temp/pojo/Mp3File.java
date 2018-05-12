package com.app.temp.pojo;

import android.support.annotation.NonNull;

import java.io.File;
import java.net.URI;

public class Mp3File extends File {
    public Mp3File(@NonNull String pathname) {
        super(pathname);
    }

    public Mp3File(String parent, @NonNull String child) {
        super(parent, child);
    }

    public Mp3File(File parent, @NonNull String child) {
        super(parent, child);
    }

    public Mp3File(@NonNull URI uri) {
        super(uri);
    }
}
