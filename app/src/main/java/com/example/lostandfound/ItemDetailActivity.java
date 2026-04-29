package com.example.lostandfound;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lostandfound.database.DatabaseHelper;

public class ItemDetailActivity extends AppCompatActivity {

    TextView textDetailPostType, textDetailName, textDetailPhone,
            textDetailDescription, textDetailDate,
            textDetailLocation, textDetailTimestamp;
    ImageView imageDetail;
    Button buttonRemove;
    DatabaseHelper db;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Link UI elements
        textDetailPostType    = findViewById(R.id.textDetailPostType);
        textDetailName        = findViewById(R.id.textDetailName);
        textDetailPhone       = findViewById(R.id.textDetailPhone);
        textDetailDescription = findViewById(R.id.textDetailDescription);
        textDetailDate        = findViewById(R.id.textDetailDate);
        textDetailLocation    = findViewById(R.id.textDetailLocation);
        textDetailTimestamp   = findViewById(R.id.textDetailTimestamp);
        imageDetail           = findViewById(R.id.imageDetail);
        buttonRemove          = findViewById(R.id.buttonRemove);

        db = new DatabaseHelper(this);

        // Retrieve all item data passed via Intent
        itemId               = getIntent().getIntExtra("itemId", -1);
        String postType      = getIntent().getStringExtra("postType");
        String name          = getIntent().getStringExtra("name");
        String phone         = getIntent().getStringExtra("phone");
        String description   = getIntent().getStringExtra("description");
        String date          = getIntent().getStringExtra("date");
        String location      = getIntent().getStringExtra("location");
        String imagePath     = getIntent().getStringExtra("imagePath");
        String timestamp     = getIntent().getStringExtra("timestamp");

        // Fill in the detail fields
        textDetailPostType.setText(postType.toUpperCase());
        textDetailName.setText(name);
        textDetailPhone.setText(phone);
        textDetailDescription.setText(description);
        textDetailDate.setText(date);
        textDetailLocation.setText(location);
        textDetailTimestamp.setText(timestamp);

        // Color the badge
        if (postType.equals("Lost")) {
            textDetailPostType.setBackgroundColor(
                    Color.parseColor("#C62828"));
        } else {
            textDetailPostType.setBackgroundColor(
                    Color.parseColor("#2E7D32"));
        }

        // Load the image
        if (imagePath != null && !imagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                imageDetail.setImageBitmap(bitmap);
            }
        }

        // Remove button
        buttonRemove.setOnClickListener(v -> {
            db.deleteItem(itemId);
            Toast.makeText(this,
                    "Item removed.", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Back button
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
    }
}
