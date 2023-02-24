package com.hawolt.rman.body;

/**
 * Created: 05/01/2023 12:37
 * Author: Twitter @hawolt
 **/

public class RMANFileBodyBundleChunk {
    private int tableOffset, compressedSize, uncompressedSize;
    private long id;

    public int getTableOffset() {
        return tableOffset;
    }

    public void setTableOffset(int tableOffset) {
        this.tableOffset = tableOffset;
    }

    public int getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(int compressedSize) {
        this.compressedSize = compressedSize;
    }

    public int getUncompressedSize() {
        return uncompressedSize;
    }

    public void setUncompressedSize(int uncompressedSize) {
        this.uncompressedSize = uncompressedSize;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
