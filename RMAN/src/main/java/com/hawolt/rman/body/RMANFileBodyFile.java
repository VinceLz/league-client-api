package com.hawolt.rman.body;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 05/01/2023 12:24
 * Author: Twitter @hawolt
 **/

public class RMANFileBodyFile {
    private List<Long> chunkIds = new ArrayList<>();
    private int offset, tableOffset, customNameOffset, fileTypeFlag, nameOffset, structSize, symlinkOffset,
            permission, languageId, fileSize, unknown2, singleChunk, unknown3;
    private String name, symLink;
    private long id, directoryId;

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

    public int getCustomNameOffset() {
        return customNameOffset;
    }

    public void setCustomNameOffset(int customNameOffset) {
        this.customNameOffset = customNameOffset;
    }

    public int getFileTypeFlag() {
        return fileTypeFlag;
    }

    public void setFileTypeFlag(int fileTypeFlag) {
        this.fileTypeFlag = fileTypeFlag;
    }

    public int getNameOffset() {
        return nameOffset;
    }

    public void setNameOffset(int nameOffset) {
        this.nameOffset = nameOffset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStructSize() {
        return structSize;
    }

    public void setStructSize(int structSize) {
        this.structSize = structSize;
    }

    public int getSymlinkOffset() {
        return symlinkOffset;
    }

    public void setSymlinkOffset(int symlinkOffset) {
        this.symlinkOffset = symlinkOffset;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getUnknown2() {
        return unknown2;
    }

    public void setUnknown2(int unknown2) {
        this.unknown2 = unknown2;
    }

    public String getSymLink() {
        return symLink;
    }

    public void setSymLink(String symLink) {
        this.symLink = symLink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(long directoryId) {
        this.directoryId = directoryId;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getSingleChunk() {
        return singleChunk;
    }

    public void setSingleChunk(int singleChunk) {
        this.singleChunk = singleChunk;
    }

    public boolean isSingleChunk() {
        return singleChunk > 0;
    }

    public int getUnknown3() {
        return unknown3;
    }

    public void setUnknown3(int unknown3) {
        this.unknown3 = unknown3;
    }

    public List<Long> getChunkIds() {
        return chunkIds;
    }

    public void addChunkId(long chunkId) {
        this.chunkIds.add(chunkId);
    }
}
