package com.kut.industryandroid.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.kut.industryandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TitleSelectActivity extends AppCompatActivity {
    ArrayList<String> titleNameList;
    ArrayAdapter<String> adapter;
    ListView listView;
    JSONArray titles;
    Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_select);
        init();
        getTitles();
    }

    public void init() {
        titleNameList = new ArrayList<>();
        listView = findViewById(R.id.titleListView);
        try {
            titles = new JSONArray(getIntent().getStringExtra("jsonobject"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleNameList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String titleName = titleNameList.get(i);
                Intent intent = new Intent(TitleSelectActivity.this, InnerTitleSelectActivity.class);
                try {
                    for (int k = 0; k < titles.length(); k++) {
                        JSONObject title = titles.getJSONObject(k);
                        if (title.getString("title_name").equals(titleName)) {
                            JSONArray innerTitles = title.getJSONArray("inner_title");
                            intent.putExtra("innertitles", innerTitles.toString());
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    void getTitles() {
        try {
            titleNameList.clear();
            for (int j = 0; j < titles.length(); j++) {
                JSONObject title = titles.getJSONObject(j);
                titleNameList.add(title.getString("title_name"));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}