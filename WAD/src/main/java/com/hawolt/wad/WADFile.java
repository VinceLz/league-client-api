package com.hawolt.wad;

import com.hawolt.generic.util.RandomAccessReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created: 15/08/2023 08:52
 * Author: Twitter @hawolt
 **/

public class WADFile {

    public WADFile(File file) throws IOException {
        this(file.toPath());
    }

    public WADFile(Path path) throws IOException {
        this(Files.readAllBytes(path));
    }

    public WADFile(byte[] b) {
        RandomAccessReader reader = new RandomAccessReader(b);
        System.out.println(reader.readString(2));
    }

    public WADFile(FileInputStream stream) throws IOException {

    }
}
