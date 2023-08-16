package com.hawolt.rman.body;

/**
 * Created: 05/01/2023 12:23
 * Author: Twitter @hawolt
 **/

public class RMANFileBodyLanguage {
    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RMANFileBodyLanguage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
