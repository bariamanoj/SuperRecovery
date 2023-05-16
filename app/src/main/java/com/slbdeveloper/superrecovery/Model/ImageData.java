package com.slbdeveloper.superrecovery.Model;

public class ImageData {
    String path;
    boolean isSelcted;
    private long dateCreate;
    private long sizeFile;

    public ImageData(String path, boolean isSelcted, long dateCreate, long sizeFile) {
        this.path = path;
        this.isSelcted = isSelcted;
        this.dateCreate = dateCreate;
        this.sizeFile = sizeFile;
    }

    public String getFilePath() {
        return path;
    }

    public void setFilePath(String path) {
        this.path = path;
    }

    public boolean getSelection() {
        return isSelcted;
    }

    public void setSelction(boolean selcted) {
        isSelcted = selcted;
    }

    public long getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(long dateCreate) {
        this.dateCreate = dateCreate;
    }

    public long getSizeFile() {
        return sizeFile;
    }

    public void setSizeFile(long sizeFile) {
        this.sizeFile = sizeFile;
    }
}
