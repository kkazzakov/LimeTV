package com.example.limetv;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Channel> channels = new ArrayList<Channel>();
    ChannelsAdapter channelsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 1; i <= 20; i++) {
            channels.add(new Channel("Первый канал","Жить здорово", false));
        }
        channelsAdapter = new ChannelsAdapter(this, channels);

        ListView lvMain = (ListView) findViewById(R.id.channels);
        lvMain.setAdapter(channelsAdapter);



    }
}