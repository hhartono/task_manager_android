package com.inerre.taskmanager;

/**
 * Created by root on 11/10/15.
 *
 */
public class StringWithTag {
    public String string;
    public String tag;

    public StringWithTag(String string, String tag) {
        this.string = string;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return string;
    }
}