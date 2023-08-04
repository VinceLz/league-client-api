package com.hawolt.rman.body;

import com.hawolt.rman.util.Hex;
import com.hawolt.rman.util.VTable;

import java.util.List;

/**
 * Created: 05/01/2023 12:23
 * Author: Twitter @hawolt
 **/

public class RMANFileBodyBundle {
    private List<RMANFileBodyBundleChunk> chunks;
    private int offset, tableOffset, headerSize;
    private VTable table;
    private byte[] skipped;
    private long id;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTableOffset() {
        return tableOffset;
    }

    public void setTableOffset(int tableOffset) {
        this.tableOffset = tableOffset;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public long getId() {
        return id;
    }

    public String getIdAsUnsignedLong() {
        return Long.toUnsignedString(id);
    }

    public void setId(long id) {
        this.id = id;
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

    public String getBundleName() {
        return String.join(".", Hex.from(id), "bundle");
    }

    public VTable getVTable() {
        return table;
    }

    public void setVTable(VTable table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return "RMANFileBodyBundle{" +
                "chunks=" + chunks +
                ", offset=" + offset +
                ", tableOffset=" + tableOffset +
                ", headerSize=" + headerSize +
                ", table=" + table +
                ", id=" + getIdAsUnsignedLong() +
                '}';
    }
}
