package com.example.qrinternet.Activities.utility;

public class Image {
    private String source;
    private byte[] binaryData;

    public Image(String _filename, byte[] _binaryData) {
        source = _filename;
        binaryData = _binaryData;
    }

    public String getSource() {
        return source;
    }
    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setSource(String _source) {
        source = _source;
    }
    public void setBinaryData(byte[] _binaryData) {
        binaryData = _binaryData;
    }
}
