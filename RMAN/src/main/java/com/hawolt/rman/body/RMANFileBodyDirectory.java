package com.hawolt.rman.body;

/**
 * Created: 05/01/2023 12:24
 * Author: Twitter @hawolt
 **/

public class RMANFileBodyDirectory {
    private int offset, tableOffset, nameOffset;
    private short directoryIdOffset, parentIdOffset, parentId;
    private String name;
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

    public int getNameOffset() {
        return nameOffset;
    }

    public void setNameOffset(int nameOffset) {
        this.nameOffset = nameOffset;
    }

    public short getDirectoryIdOffset() {
        return directoryIdOffset;
    }

    public void setDirectoryIdOffset(short directoryIdOffset) {
        this.directoryIdOffset = directoryIdOffset;
    }

    public short getParentIdOffset() {
        return parentIdOffset;
    }

    public void setParentIdOffset(short parentIdOffset) {
        this.parentIdOffset = parentIdOffset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getParentId() {
        return parentId;
    }

    public void setParentId(short parentId) {
        this.parentId = parentId;
    }
}
