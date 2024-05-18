package com.vs2010wy.tool.model;


import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {
    private String fullPath;
    private String fileName;
    private Location location;
    private String category;
    private String detail;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    private LocalDateTime localDateTime;

    public Comment() {
    }

    public Comment(int index,String fullPath, String fileName, Location location, String category,String detail) {
        this.fullPath = fullPath;
        this.fileName = fileName;
        this.location = location;
        this.index = index;
        this.detail = detail;
        this.setCategory(category);
        this.localDateTime = LocalDateTime.now();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(fullPath, comment.fullPath) && Objects.equals(location, comment.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullPath, location);
    }
}
