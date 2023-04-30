package com.example.qrinternet.Activities.utility;

import com.google.firebase.firestore.Blob;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Image {
    private String source;
    private byte[] binaryData;

    public Image() {
        source = "noname.png";
        binaryData = new byte[7605];
    }
    public Image(String _filename, byte[] _binaryData) {
        source = _filename;
        binaryData = _binaryData;
    }

    public String getSource() {
        return source;
    }
    public List<Integer> getBinaryData() {
        Byte[] temp_b = ArrayUtils.toObject(binaryData);
        List<Integer> temp_i = new ArrayList<>();
        for (Byte b : temp_b) {
            temp_i.add(b == null ? null : b.intValue());
        }
        return temp_i;
    }
    public byte[] getRawData() {
        return binaryData;
    }

    public void setSource(String _source) {
        source = _source;
    }
    public void setBinaryData(List<Integer> _binaryData) {
        int size = _binaryData.size();
        byte[] temp = new byte[size];

        for (int i = 0; i < size; i++) {
            temp[i] = _binaryData.get(i).byteValue();
        }

        binaryData = temp;
    }
}
