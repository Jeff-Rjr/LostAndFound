package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    Button buttonCreateAdvert, buttonShowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        buttonCreateAdvert = findViewById(R.id.buttonCreateAdvert);
        buttonShowItems    = findViewById(R.id.buttonShowItems);

        buttonCreateAdvert.setOnClickListener(v ->
                startActivity(new Intent(this, AddItemActivity.class)));

        buttonShowItems.setOnClickListener(v ->
                startActivity(new Intent(this, ItemListActivity.class)));
    }
}