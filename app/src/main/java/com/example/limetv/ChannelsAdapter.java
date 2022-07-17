package com.example.limetv;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChannelsAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater LInflater;
    private List<Channel> allChannels;
    private List<Channel> filteredChannels;
    private ChannelFilter channelFilter = new ChannelFilter();
    ArrayList<Channel> favoriteChannels = new ArrayList<Channel>();
    ArrayList<Integer> fav =  new ArrayList<Integer>();


    ChannelsAdapter(Context context, ArrayList<Channel> channels) {
        allChannels = channels;
        filteredChannels = channels;
        LInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filteredChannels.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredChannels.get(position);
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
            ((ImageView) view.findViewById(R.id.channel_pic)).setImageBitmap(ch.image);

            ToggleButton togg;

            togg = (ToggleButton) view.findViewById(R.id.btnIsFave);
            setChecked(togg,position);

        togg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    if(!favoriteChannels.contains(allChannels.get(position))) {
                        favoriteChannels.add(allChannels.get(position));
                        fav.add(position);
                        allChannels.get(position).isFavorite = true;
                    }
                } else {
                    favoriteChannels.remove(allChannels.get(position));
                    for(int i=0;i<fav.size();i++)
                    {
                        if(fav.get(i)==position)
                        {
                            fav.remove(i);
                        }
                    }
                   allChannels.get(position).isFavorite = false;
                }
            }
        });

        return view;
    }

    Channel getChannel(int position) {
        return ((Channel) getItem(position));
    }

    public Filter getFilter() {
        return channelFilter;
    }

    private class ChannelFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String filterString = charSequence.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Channel> list = allChannels;

            int count = list.size();
            final ArrayList<Channel> nlist = new ArrayList<Channel>(count);

            Channel filterableChannel;
            for (int i = 0; i < count; i++) {
                filterableChannel = list.get(i);
                if (filterableChannel.name.toLowerCase().contains(filterString)){
                    nlist.add(filterableChannel);
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredChannels = (ArrayList<Channel>) filterResults.values;
            notifyDataSetChanged();
        }
    }

    public void setChecked(ToggleButton a,int position)
    {
         if(allChannels.get(position).isFavorite==true)
         {
             a.setChecked(true);
         }else a.setChecked(false);
    }
}
