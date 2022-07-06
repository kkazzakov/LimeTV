package com.example.limetv;

import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class ChannelsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater LInflater;
    ArrayList<Channel> objects;

    ChannelsAdapter(Context context, ArrayList<Channel> channels) {
        ctx = context;
        objects = channels;
        LInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LInflater.inflate(R.layout.channels_item, parent, false);
        }
            Channel ch = getChannel(position);

            ((TextView) view.findViewById(R.id.channelName)).setText(ch.name);
            ((TextView) view.findViewById(R.id.currentShow)).setText(ch.currentShow);





        return view;
    }

    Channel getChannel(int position) {
        return ((Channel) getItem(position));
    }
}
