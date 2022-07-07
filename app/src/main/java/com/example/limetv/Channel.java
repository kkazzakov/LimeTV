package com.example.limetv;

import android.graphics.Bitmap;

public class Channel implements Comparable<Channel> {
    Integer number;
    String name;
    String currentShow;
    boolean isFavorite;
    Bitmap image;

    Channel(Integer _number, String _name) {
        number = _number;
        name = _name;
        currentShow = "-";
    }

    Channel(Integer _number, String _name, String _currentShow, Bitmap _image, boolean _isFaforite) {
        number = _number;
        name = _name;
        currentShow = _currentShow;
        image = _image;
        isFavorite = _isFaforite;
    }

    @Override
    public int compareTo(Channel channel) {
        int cmp = this.number.compareTo(channel.number);
        return cmp != 0 ? cmp : this.number.compareTo(channel.number);
    }
}
