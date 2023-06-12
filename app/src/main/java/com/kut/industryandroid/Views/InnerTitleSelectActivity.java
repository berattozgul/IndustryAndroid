package com.kut.industryandroid.Views;

import android.content.Intent;
import android.os.Bundle;
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

public class InnerTitleSelectActivity extends AppCompatActivity {
    ArrayList<String> innerTitleNameList;
    ArrayAdapter<String> adapter;
    ListView listView;
    JSONArray innerTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_title_select);
        init();
        getInnerTitles();
    }

    public void init() {
        innerTitleNameList = new ArrayList<>();
        try {
            innerTitles = new JSONArray(getIntent().getStringExtra("innertitles"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        listView = findViewById(R.id.inner_title_list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, innerTitleNameList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String titleName = innerTitleNameList.get(i);
                Intent intent = new Intent(InnerTitleSelectActivity.this, ViewerActivity.class);
                try {
                    for (int k = 0; k < innerTitles.length(); k++) {
                        JSONObject title = innerTitles.getJSONObject(k);
                        if (title.getString("name").equals(titleName)) {
                            intent.putExtra("innertitles",innerTitles.toString());
                            intent.putExtra("name", title.getString("name"));
                            intent.putExtra("description", title.getString("description"));
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }


    public void getInnerTitles() {
        try {
            innerTitleNameList.clear();
            for (int i = 0; i < innerTitles.length(); i++) {
                JSONObject innerTitle = innerTitles.getJSONObject(i);
                innerTitleNameList.add(innerTitle.getString("name"));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}