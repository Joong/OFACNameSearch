package com.jhl.ofac;

/**
 * User: jolee
 * Date: 9/17/14
 */
public class OFACSearchResult {
    private String name = "";
    private String address = "";
    private String type = "";
    private String programs = "";
    private String list = "";
    private String score = "";

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrograms(String programs) {
        this.programs = programs;
    }

    public void setList(String list) {
        this.list = list;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getType() {
        return this.type;
    }

    public String getPrograms() {
        return this.programs;
    }

    public String getList() {
        return this.list;
    }

    public String getScore() {
        return this.score;
    }

    public String toString() {
        String delimiter = ",";
        StringBuilder sb = new StringBuilder();
        sb.append(getName().trim()).append(delimiter)
                .append(getAddress().trim()).append(delimiter)
                .append(getType()).append(delimiter)
                .append(getPrograms()).append(delimiter)
                .append(getList()).append(delimiter)
                .append(getScore());
        return sb.toString();
    }
}
