package com.vs2010wy.tool.model;

public class Comment {
    private String fullPath;
    private String fileName;
    private Location location;
    private String category;
    private String detail;

    public Comment() {
    }

    public Comment(String fullPath, String fileName, Location location, String detail) {
        this.fullPath = fullPath;
        this.fileName = fileName;
        this.location = location;
        this.detail = detail;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "fullPath='" + fullPath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", location=" + location +
                ", detail='" + detail + '\'' +
                '}';
    }
}
