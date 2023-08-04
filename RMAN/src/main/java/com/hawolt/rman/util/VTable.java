package com.hawolt.rman.util;

import java.util.Arrays;

/**
 * Created: 03/08/2023 21:36
 * Author: Twitter @hawolt
 **/

public class VTable {
    private int objects;
    private int size;
    private int[] offsets;

    public static VTable read(RandomAccessReader reader) {
        VTable vTable = new VTable();
        vTable.setSize(reader.readShort());
        vTable.setObjects(reader.readShort());
        int[] offsets = new int[vTable.getObjects()];
        for (int j = 0; j < vTable.getObjects(); j++) {
            offsets[j] = (reader.readShort() & 0xFFFF);
        }
        vTable.setOffsets(offsets);
        return vTable;
    }

    public int getObjects() {
        return objects;
    }

    public void setObjects(int objects) {
        this.objects = objects;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public void setOffsets(int[] offsets) {
        this.offsets = offsets;
    }

    @Override
    public String toString() {
        return "VTable{" +
                "objects=" + objects +
                ", size=" + size +
                ", offsets=" + Arrays.toString(offsets) +
                '}';
    }
}
