package com.hawolt.rman.body;

/**
 * Created: 05/01/2023 12:37
 * Author: Twitter @hawolt
 **/

public class RMANFileBodyBundleChunk {
    private int compressedSize;
    private int uncompressedSize;
    private String chunkId;

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

    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }

    @Override
    public String toString() {
        return "RMANFileBodyBundleChunk{" +
                "compressedSize=" + compressedSize +
                ", uncompressedSize=" + uncompressedSize +
                ", chunkId='" + chunkId + '\'' +
                '}';
    }
}
