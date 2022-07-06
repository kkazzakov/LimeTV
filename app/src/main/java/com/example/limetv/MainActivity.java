package com.example.limetv;

import android.app.ProgressDialog;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.example.limetv.util.JSONReader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Channel> channels = new ArrayList<>();
    ChannelsAdapter channelsAdapter;
    Handler handler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FetchJson().start();

        channelsAdapter = new ChannelsAdapter(MainActivity.this, channels);
        ListView lvMain = (ListView) findViewById(R.id.channels);
        lvMain.setAdapter(channelsAdapter);
    }

    private class FetchJson extends Thread {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                JSONObject json = JSONReader.getJson("https://limehd.online/playlist");
                for (int i = 0; i < json.getJSONArray("channels").length(); i++) {
                    JSONObject channel = json.getJSONArray("channels").getJSONObject(i);

                    if (!channel.isNull("current")) {
                        channels.add(new Channel(channel.getString("name_ru"),
                                channel.getJSONObject("current").getString("title"), false));
                    } else {
                        channels.add(new Channel(channel.getString("name_ru"), "-", false));
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    channelsAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}