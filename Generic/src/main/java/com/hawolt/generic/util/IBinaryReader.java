package com.hawolt.generic.util;

/**
 * Created: 01/01/2023 03:08
 * Author: Twitter @hawolt
 **/

public interface IBinaryReader {
    int readInt();

    long readLong();

    byte readByte();

    short readShort();

    byte[] readBytes(int n);

    String readString(int length);
}
