package com.timetrace.objects;

/**
 * Created by Atomu on 2014/11/27.
 */
public class ProcessInfo {
    /**
     * the most important attrs, packName is the unique identifier, and importance records the status of a process
     */
    protected String packName;
    protected int importance;

    public String getPacName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    @Override
    public String toString() {
        return "{package : " + packName + ", importance : " + importance + "}";
    }
}
