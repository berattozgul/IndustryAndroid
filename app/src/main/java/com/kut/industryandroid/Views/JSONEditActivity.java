package com.kut.industryandroid.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kut.industryandroid.Models.DecisionTree;
import com.kut.industryandroid.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JSONEditActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String localJson;
    private JSONObject localJsonObject;
    private String localVersion;
    private String firebaseJson;
    private JSONObject firebaseJsonObject;
    private String firebaseVersion;
    private EditText jsonEditET;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonedit);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("JSONCode");
        localJson = getJSONFromFile();
        jsonEditET = findViewById(R.id.json_edit_text);
        saveButton = findViewById(R.id.save_button);
        try {
            localJsonObject = new JSONObject(localJson);
            localVersion = localJsonObject.getString("version");
            localVersion.replaceAll("[^0-9]", "");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        getJSONFromFirebaseDatabase();
    }

    private String getJSONFromFile() {
        String json = null;
        try {
            // Read JSON file from assets directory
            InputStream inputStream = getAssets().open("example.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void getJSONFromFirebaseDatabase() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebaseJson = snapshot.child(localVersion).getValue(String.class);
                try {
                    firebaseJsonObject = new JSONObject(firebaseJson);
                    firebaseVersion = firebaseJsonObject.getString("version");
                    jsonEditET.setText(formatJson(firebaseJsonObject.toString()));
                    firebaseVersion.replaceAll("[^0-9]", "");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (localVersion.compareTo(firebaseVersion) < 0) {
                    try {
                        // Overwrite example.json in assets with the Firebase version
                        writeJSONToFile(firebaseJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }

    private String formatJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.toString(4); // Use 4 spaces for indentation
        } catch (JSONException e) {
            e.printStackTrace();
            return jsonString; // Return the unformatted string if there is an error
        }
    }


    private void writeJSONToFile(String json) throws IOException {
        // Open the input stream for the asset file
        InputStream inputStream = getAssets().open("example.json");

        // Create a file object representing the file in the internal storage directory
        File file = new File(getFilesDir(), "example.json");

        // Create the output stream for the internal storage file
        FileOutputStream outputStream = new FileOutputStream(file);

        // Copy the contents of the asset file to the internal storage file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        // Close the streams
        inputStream.close();
        outputStream.close();

        // Write the JSON string to the internal storage file
        FileWriter writer = new FileWriter(file, true);
        writer.append(json);
        writer.flush();
        writer.close();

        Toast.makeText(this, "example.json updated with Firebase version", Toast.LENGTH_SHORT).show();
    }



    public void saveAndUpload(View view) throws JSONException, IOException {
        String newJson = jsonEditET.getText().toString();
        String versionString = firebaseVersion;
        String versionDigitsOnly = versionString.replaceAll("[^0-9]", "");
        int versionNumber = Integer.parseInt(versionDigitsOnly);
        versionNumber++;
        reference.child(String.valueOf(versionNumber))
                .setValue(newJson)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(JSONEditActivity.this, "Uploaded to Firebase!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        writeJSONToFile(newJson);
    }
}