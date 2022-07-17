package com.example.limetv;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.limetv.util.JSONReader;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ArrayList<Channel> channels = new ArrayList<>();
    ArrayList<Integer> favoriteCahnnelsID = new ArrayList<Integer>();
    ChannelsAdapter channelsAdapter;
    ChannelsAdapter channelsAdapterFavorite;
    Handler handler = new Handler(Looper.getMainLooper());
    ProgressBar progressBar;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String save_key = "favorites";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favoriteCahnnelsID = loadArray(save_key,MainActivity.this);
        setChannels(favoriteCahnnelsID);

        channelsAdapter = new ChannelsAdapter(MainActivity.this, channels);
        channelsAdapterFavorite = new ChannelsAdapter(MainActivity.this,channelsAdapter.favoriteChannels);

        ListView lvMain = (ListView) findViewById(R.id.channels);

        lvMain.setAdapter(channelsAdapter);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        channelsAdapter.fav = favoriteCahnnelsID;
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                            saveArray(channelsAdapter.fav,"favorites",MainActivity.this);
                        lvMain.setAdapter(channelsAdapter);
                        break;
                    case 1:
                            saveArray(channelsAdapter.fav,"favorites",MainActivity.this);
                        for (int i=0;i<channels.size();i++)
                        {
                            if(channels.get(i).isFavorite==true && !(channelsAdapter.favoriteChannels.contains(channels.get(i))))
                                channelsAdapter.favoriteChannels.add(channels.get(i));
                            if(channels.get(i).isFavorite==false && channelsAdapter.favoriteChannels.contains(channels.get(i)))
                                channelsAdapter.favoriteChannels.remove(channels.get(i));
                        }
                        lvMain.setAdapter(channelsAdapterFavorite);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                channelsAdapter.getFilter().filter(s.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                channelsAdapter.getFilter().filter(s.toString());
                return false;
            }
        });



    }
    public boolean saveArray(ArrayList<Integer> array, String arrayName, Context mContext) {
        prefs = mContext.getSharedPreferences(save_key, 0);
        editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putInt(arrayName + "_" + i, array.get(i));
        return editor.commit();
    }
    public ArrayList<Integer> loadArray(String arrayName, Context mContext) {
        prefs = mContext.getSharedPreferences(save_key, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        ArrayList<Integer> array =  new ArrayList<Integer>();
        for(int i=0;i<size;i++)
            array.add(prefs.getInt(arrayName + "_" + i, 0));
        return array;
    }
    private void setChannels(ArrayList<Integer> favoriteCahnnelsID) {
        Executors.newSingleThreadExecutor().execute(() -> {
            handler.post(() -> {
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            });

            try {
                JSONObject json = JSONReader.getJson("https://limehd.online/playlist");
                for (int i = 0; i < json.getJSONArray("channels").length(); i++) {
                    JSONObject jsonChannel = json.getJSONArray("channels").getJSONObject(i);
                    Channel channel = new Channel(jsonChannel.getInt("number"),
                            jsonChannel.getString("name_ru"));
                    if(favoriteCahnnelsID.contains(i))
                        channel.isFavorite = true;
                    else
                        channel.isFavorite = false;


                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {
                            channel.image = BitmapFactory.decodeStream(new URL(jsonChannel.getString("image")).openStream());
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    });

                    if (!jsonChannel.isNull("current")) {
                        channel.currentShow = jsonChannel.getJSONObject("current").getString("title");
                    }

                    channels.add(channel);
                }

                   //Collections.sort(channels);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                progressBar.setVisibility(View.INVISIBLE);

                channelsAdapter.notifyDataSetChanged();
            });
        });
    }
}