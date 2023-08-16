package com.hawolt.rman.body;

import java.util.Arrays;
import java.util.List;

/**
 * Created: 05/01/2023 12:23
 * Author: Twitter @hawolt
 **/

public class RMANFileBodyBundle {
    private int headerSize;
    private String bundleId;
    private byte[] skipped;
    private List<RMANFileBodyBundleChunk> chunks;

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public byte[] getSkipped() {
        return skipped;
    }

    public void setSkipped(byte[] skipped) {
        this.skipped = skipped;
    }

    public List<RMANFileBodyBundleChunk> getChunks() {
        return chunks;
    }

    public void setChunks(List<RMANFileBodyBundleChunk> chunks) {
        this.chunks = chunks;
    }

    @Override
    public String toString() {
        return "RMANFileBodyBundle{" +
                "headerSize=" + headerSize +
                ", bundleId='" + bundleId + '\'' +
                ", skipped=" + Arrays.toString(skipped) +
                ", chunks=" + chunks +
                '}';
    }
}
