package com.kut.industryandroid.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kut.industryandroid.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewerActivity extends AppCompatActivity {
    TextView nameTw, descriptionTw;
    String name;
    int counter = 0;
    JSONArray innerTitles;
    ArrayList<String> innerTitleNameList;
    String description;
    String imageSource;
    ImageView imageView;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        init();
        getInnerTitles();
        useAdapter();
    }

    private void useAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, innerTitleNameList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void init() {
        name = getIntent().getStringExtra("name");
        nameTw = findViewById(R.id.name);
        imageView = findViewById(R.id.imageView);
        descriptionTw = findViewById(R.id.description);
        spinner = findViewById(R.id.view_spinner);
        innerTitleNameList = new ArrayList<>();
        try {
            innerTitles = new JSONArray(getIntent().getStringExtra("innertitles"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        descriptionTw.setText(description);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (counter > 0) {
                    String titleName = innerTitleNameList.get(i);
                    try {
                        for (int k = 0; k < innerTitles.length(); k++) {
                            JSONObject title = innerTitles.getJSONObject(k);
                            if (title.getString("name").equals(titleName)) {
                                name = title.getString("name");
                                System.out.println(name);
                                description = title.getString("description");
                                nameTw.setText(name);
                                imageSource = getImage();
                                descriptionTw.setText(description);
                                changePicture();
                                getSupportActionBar().setTitle(name);
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else if (counter == 0) {
                    String titleName = name;
                    try {
                        for (int k = 0; k < innerTitles.length(); k++) {
                            JSONObject title = innerTitles.getJSONObject(k);
                            if (title.getString("name").equals(titleName)) {
                                name = title.getString("name");
                                System.out.println(name);
                                description = title.getString("description");
                                nameTw.setText(name);
                                imageSource = getImage();
                                descriptionTw.setText(description);
                                changePicture();
                                getSupportActionBar().setTitle(name);
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    counter++;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        changePicture();
    }

    public void changePicture() {
        imageSource = getImage();
        if (!name.equals("Introduction") && !name.equals("Hardware")) {
            Picasso.get().load(imageSource).into(imageView);
        }
    }

    public String getImage() {
        switch (name) {
            case "AR/RS for palettes":
                return "https://lh5.googleusercontent.com/9Ug38GuTInzTBpbFteddIjn-xtfiFDDpoUHJh6cdQihfLBbNPnf2TYskpc6ea6ZWL4tN-_GXcYfb0UoYMA-wVbcXTYXDgfMig0n2zd0dpHXzjwgMhw2wyc40ckHqGP3bmnx_MuJVczU-y6KQGO2AaDQ";
            case "Robot Assembly":
                return "https://lh4.googleusercontent.com/rn73qC8Elj9L762cXxQCG8-dAr95LYvks6rMCnSl9zRcQ0aPx0_jlzM_K-llF-siX-0tGh__-v-DuiHCkskq9A4WfPymrDv6wfxxoSrtZK_3TEP-8LOPG6SV4wfCeSlWyN9b7x4zuTDNIYQs13rotpc";
            case "Basic Module Branch and Linear":
                return "https://lh6.googleusercontent.com/nzwZ0qiLmEU7hZT39hFr7WnPeZjfbYyu1zgsiUo6ynGFdzCTIxANGhIz--nQM_iqoEc-ASj4uXuAbKWUlYM1TqlqPSR9LMTjMJcpBcOrrVaOZqnzltrsYfqSTIeBq3myHqRZPRWXx_bhH3rE5K3q_-4";
            case "Camera Inspection":
                return "https://lh5.googleusercontent.com/rZUBZs3_8fHZZKq8HCHgVxAtuOYd-T_5gt_lE36RBUCtBUnxmGCjD-cViPMH1IjyVGKsznyyMNl19ZZWds-lUZL1DoAGOrT9X43siUop6vRb3r6MbECsf0FgJwY9gyfCgrNfVjMWaAkJ_Y8tn_e2slo";
            case "Heat Tunnel":
                return "https://lh4.googleusercontent.com/iF0-0jkzy53U9AFLmScZqeIH9r4aO-esMn1AhQH0DPFv3y1ctU36XMus58V4yCM8tz7fZJk_ZGItNBBmfJKygilpBlKfsCpNYc0r3sVXw_adX1uukc2iTdO-vWU7IFzJljXoCtEiyPdwWTYxMVKZZPA";
            case "Magazine":
                return "https://lh5.googleusercontent.com/kSh1ALmD8saPu9kyej6DdVHj9jotCVBOFVKsBLxVIYII16Q1FBxO5YDoIUsQhK2LGwbuM8Wt_yRK9z8maNXBPXcESR0L6CH28Qq-mRSz9UsqZTmklpXOAuAxt9ysHuEq9J6OuGqU9u0orIyWMQLrrQo";
            case "Press":
                return "https://lh3.googleusercontent.com/yfQ9zE9K176fMwbJf06N3H0RRxc7geOhAWaBVBXieRdo9M0Bo64sogfj3brO9ZOAMNMh32oBjOBeQQbqPJnMNUhpl1N_zF-60tD-yZjKU9HhwUX1PkbPRnatKQZXIGQciPXgTS0Q6o9RzheNFIwU1YU";
            case "Robotino CP-Factory":
                return "https://lh5.googleusercontent.com/h9NgFAniLsVLPfKEwGmrXI0dWnaU3_K1Tf-YsmXBWrmAsB6d80VUmBlWUCyfYZrcy2O6d1sELlfyWfcHjhtNZCrW37GxXMeHWwDGUynkB9BoD9Uwwa1efM7t9XUWD8TJuYMoMB9AmsXdFflnAPC4vZs";
            default:
                imageView.setVisibility(View.GONE);
                return "";
        }
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