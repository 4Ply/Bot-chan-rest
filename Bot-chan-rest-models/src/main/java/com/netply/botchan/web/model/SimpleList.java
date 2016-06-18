package com.netply.botchan.web.model;

import java.util.ArrayList;

public class SimpleList {
    private ArrayList<String> list;


    public SimpleList() {
    }

    public SimpleList(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String> getList() {
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleList that = (SimpleList) o;

        return list != null ? list.equals(that.list) : that.list == null;

    }

    @Override
    public int hashCode() {
        return list != null ? list.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SimpleList{" +
                "list=" + list +
                '}';
    }
}
