package com.venkat.macyssdcardscanner.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by venkat on 4/29/18.
 */

public class Details {
    HashMap<String, Integer> extMap = new HashMap<>();

    HashMap<String, Float> sizeMap = new HashMap<>();


    float avgSize;

    public float getAvgSize() {
        return avgSize;
    }

    public void setAvgSize(float avgSize) {
        this.avgSize = avgSize;
    }

    public HashMap<String, Integer> getExtMap() {
        return extMap;
    }

    public void setExtMap(HashMap<String, Integer> extMap) {
        this.extMap = extMap;
    }

    public HashMap<String, Float> getSizeMap() {
        return sizeMap;
    }

    public void setSizeMap(HashMap<String, Float> sizeMap) {
        this.sizeMap = sizeMap;
    }

}
