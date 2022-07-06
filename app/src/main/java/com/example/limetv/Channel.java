package com.example.limetv;

import android.graphics.Bitmap;

public class Channel {
    String name;
    String currentShow;
    boolean isFavorite;
    Bitmap image;

    Channel(String _name) {
        name = _name;
        currentShow = "-";
    }

    Channel(String _name, String _currentShow, Bitmap _image, boolean _isFaforite) {
        name = _name;
        currentShow = _currentShow;
        image = _image;
        isFavorite = _isFaforite;
    }
}
