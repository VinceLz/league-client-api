package com.hawolt.rman.body;

/**
 * Created: 05/01/2023 13:40
 * Author: Twitter @hawolt
 **/

public class RMANFileBodyBundleChunkInfo {
    private final int offsetToChunk, compressedSize;
    private final String bundleId, chunkId;

    public RMANFileBodyBundleChunkInfo(String bundleId, String chunkId, int offsetToChunk, int compressedSize) {
        this.compressedSize = compressedSize;
        this.offsetToChunk = offsetToChunk;
        this.bundleId = bundleId;
        this.chunkId = chunkId;
    }

    public int getOffsetToChunk() {
        return offsetToChunk;
    }

    public int getCompressedSize() {
        return compressedSize;
    }

    public String getBundleId() {
        return bundleId;
    }

    public String getChunkId() {
        return chunkId;
    }

    @Override
    public String toString() {
        return "RMANFileBodyBundleChunkInfo{" +
                "offsetToChunk=" + offsetToChunk +
                ", compressedSize=" + compressedSize +
                ", bundleId=" + bundleId +
                ", chunkId=" + chunkId +
                '}';
    }
}
