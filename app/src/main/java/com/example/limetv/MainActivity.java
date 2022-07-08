package com.example.limetv;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.limetv.util.JSONReader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ArrayList<Channel> channels = new ArrayList<>();
    ChannelsAdapter channelsAdapter;
    Handler handler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setChannels();

        channelsAdapter = new ChannelsAdapter(MainActivity.this, channels);
        ListView lvMain = (ListView) findViewById(R.id.channels);
        lvMain.setAdapter(channelsAdapter);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);

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

    private void setChannels() {
        Executor channelLoader = Executors.newSingleThreadExecutor();
        channelLoader.execute(() -> {
            handler.post(() -> {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Загрузка каналов");
                progressDialog.setCancelable(false);
                progressDialog.show();
            });

            try {
                JSONObject json = JSONReader.getJson("https://limehd.online/playlist");
                for (int i = 0; i < json.getJSONArray("channels").length(); i++) {
                    JSONObject jsonChannel = json.getJSONArray("channels").getJSONObject(i);
                    Channel channel = new Channel(jsonChannel.getInt("number"),
                            jsonChannel.getString("name_ru"));
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

                    Collections.sort(channels);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                channelsAdapter.notifyDataSetChanged();
            });
        });
    }
}