package com.vs2010wy.tool.model;

public class Location {
    private Position start;
    private Position end;

    public Location(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public Position getStart() {
        return start;
    }

    public void setStart(Position start) {
        this.start = start;
    }

    public Position getEnd() {
        return end;
    }

    public void setEnd(Position end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Location{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
