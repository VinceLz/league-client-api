package com.hawolt.rman.body;

import java.util.List;

/**
 * Created: 05/01/2023 12:15
 * Author: Twitter @hawolt
 **/

public class RMANFileBody {
    private RMANFileBodyHeader header;
    private List<RMANFileBodyFile> files;
    private List<RMANFileBodyBundle> bundles;
    private List<RMANFileBodyLanguage> languages;
    private List<RMANFileBodyDirectory> directories;
    private int headerOffset;

    public RMANFileBodyHeader getHeader() {
        return header;
    }

    public void setHeader(RMANFileBodyHeader header) {
        this.header = header;
    }

    public List<RMANFileBodyFile> getFiles() {
        return files;
    }

    public void setFiles(List<RMANFileBodyFile> files) {
        this.files = files;
    }

    public List<RMANFileBodyBundle> getBundles() {
        return bundles;
    }

    public void setBundles(List<RMANFileBodyBundle> bundles) {
        this.bundles = bundles;
    }

    public List<RMANFileBodyLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<RMANFileBodyLanguage> languages) {
        this.languages = languages;
    }

    public List<RMANFileBodyDirectory> getDirectories() {
        return directories;
    }

    public void setDirectories(List<RMANFileBodyDirectory> directories) {
        this.directories = directories;
    }

    public int getHeaderOffset() {
        return headerOffset;
    }

    public void setHeaderOffset(int headerOffset) {
        this.headerOffset = headerOffset;
    }

    @Override
    public String toString() {
        return "RMANFileBody{" +
                "header=" + header +
                ", files=" + files +
                ", bundles=" + bundles +
                ", languages=" + languages +
                ", directories=" + directories +
                ", headerOffset=" + headerOffset +
                '}';
    }
}
