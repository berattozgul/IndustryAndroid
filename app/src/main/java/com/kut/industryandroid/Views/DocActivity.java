package com.kut.industryandroid.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.kut.industryandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DocActivity extends AppCompatActivity {

    int index;
    Button bt;
    Spinner spinner;
    TextView qrCodeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        init();
    }
    void init(){
        index=0;
        spinner=findViewById(R.id.spinner2);
        bt=findViewById(R.id.button);
        qrCodeText=findViewById(R.id.qrCodeText);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text=adapterView.getItemAtPosition(i).toString();
                if(text.equals("Turkish")){
                    index=0;
                }else if(text.equals("English")){
                    index=1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                index=0;
            }
        });
        qrCodeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DocActivity.this,QrCodeScanActivity.class));
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new fetchData().start();
            }
        });

    }

    class fetchData extends Thread {
        String data = "";

        @Override
        public void run() {

            String urll = "https://pastebin.com/raw/Uz9sx3XT";
            try {
                URL url = new URL(urll);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    data = data + line;
                }
                if (!data.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(data);
                    Intent intent=new Intent(DocActivity.this,TitleSelectActivity.class);
                    JSONObject industryObject = jsonObject.getJSONObject("industry");

                    JSONArray languages = industryObject.getJSONArray("language");
                    JSONArray titles=languages.getJSONObject(index).getJSONArray("title");
                    intent.putExtra("jsonobject",titles.toString());
                    startActivity(intent);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}