package com.hawolt.rman.body;

/**
 * Created: 05/01/2023 12:15
 * Author: Twitter @hawolt
 **/

public class RMANFileBodyHeader {
    private int tableOffset, bundleListOffset, languageListOffset, fileListOffset, folderListOffset, keyHeaderOffset, unknownOffset;

    public int getTableOffset() {
        return tableOffset;
    }

    public void setTableOffset(int tableOffset) {
        this.tableOffset = tableOffset;
    }

    public int getBundleListOffset() {
        return bundleListOffset;
    }

    public void setBundleListOffset(int bundleListOffset) {
        this.bundleListOffset = bundleListOffset;
    }

    public int getLanguageListOffset() {
        return languageListOffset;
    }

    public void setLanguageListOffset(int languageListOffset) {
        this.languageListOffset = languageListOffset;
    }

    public int getFileListOffset() {
        return fileListOffset;
    }

    public void setFileListOffset(int fileListOffset) {
        this.fileListOffset = fileListOffset;
    }

    public int getFolderListOffset() {
        return folderListOffset;
    }

    public void setFolderListOffset(int folderListOffset) {
        this.folderListOffset = folderListOffset;
    }

    public int getKeyHeaderOffset() {
        return keyHeaderOffset;
    }

    public void setKeyHeaderOffset(int keyHeaderOffset) {
        this.keyHeaderOffset = keyHeaderOffset;
    }

    public int getUnknownOffset() {
        return unknownOffset;
    }

    public void setUnknownOffset(int unknownOffset) {
        this.unknownOffset = unknownOffset;
    }

    @Override
    public String toString() {
        return "RMANFileBodyHeader{" +
                "tableOffset=" + tableOffset +
                ", bundleListOffset=" + bundleListOffset +
                ", languageListOffset=" + languageListOffset +
                ", fileListOffset=" + fileListOffset +
                ", folderListOffset=" + folderListOffset +
                ", keyHeaderOffset=" + keyHeaderOffset +
                ", unknownOffset=" + unknownOffset +
                '}';
    }
}
