package com.hawolt.rman.body;

/**
 * Created: 05/01/2023 13:40
 * Author: Twitter @hawolt
 **/

public class RMANFileBodyBundleChunkInfo {
    private final int offsetToChunk, compressedSize;
    private final long bundleId, chunkId;

    public RMANFileBodyBundleChunkInfo(long bundleId, long chunkId, int offsetToChunk, int compressedSize) {
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

    public long getBundleId() {
        return bundleId;
    }

    public long getChunkId() {
        return chunkId;
    }
}
