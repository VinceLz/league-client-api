package com.hawolt.generic.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Created: 05/01/2023 12:06
 * Author: Twitter @hawolt
 **/

public class RandomAccessReader implements IBinaryReader {
    private final byte[] b;
    private int position;

    public RandomAccessReader(File file) throws IOException {
        this(file.toPath());
    }

    public RandomAccessReader(Path path) throws IOException {
        this(Files.readAllBytes(path));
    }

    public RandomAccessReader(byte[] b) {
        this.b = b;
    }

    public void seek(int position) {
        this.position = position;
    }

    public int position() {
        return position;
    }


    @Override
    public short readShort() {
        byte[] copy = Arrays.copyOfRange(b, position, position + 2);
        this.position += 2;
        return (short) ((copy[1] & 0xFF) << 8 | (copy[0] & 0xFF));
    }

    @Override
    public int readInt() {
        byte[] copy = Arrays.copyOfRange(b, position, position + 4);
        this.position += 4;
        return copy[3] << 24 | (copy[2] & 0xFF) << 16 | (copy[1] & 0xFF) << 8 | (copy[0] & 0xFF);
    }

    @Override
    public long readLong() {
        byte[] copy = Arrays.copyOfRange(b, position, position + 8);
        this.position += 8;
        return ((copy[7] & 0xFFL) << 56) |
                ((copy[6] & 0xFFL) << 48) |
                ((copy[5] & 0xFFL) << 40) |
                ((copy[4] & 0xFFL) << 32) |
                ((copy[3] & 0xFFL) << 24) |
                ((copy[2] & 0xFFL) << 16) |
                ((copy[1] & 0xFFL) << 8) |
                ((copy[0] & 0xFFL));
    }

    @Override
    public String readString(int n) {
        byte[] b = Arrays.copyOfRange(this.b, position, position + n);
        this.position += n;
        return new String(b);
    }

    @Override
    public byte readByte() {
        return b[position++];
    }

    @Override
    public byte[] readBytes(int n) {
        byte[] b = Arrays.copyOfRange(this.b, position, position + n);
        this.position += n;
        return b;
    }
}
