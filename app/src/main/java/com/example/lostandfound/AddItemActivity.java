package com.example.lostandfound;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lostandfound.database.DatabaseHelper;
import com.example.lostandfound.database.LostFoundItem;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    RadioGroup radioGroupPostType;
    EditText editName, editPhone, editDescription, editLocation;
    Button buttonPickDate, buttonPickImage, buttonSave;
    TextView textSelectedDate;
    ImageView imagePreview;

    String selectedDate      = "";
    String selectedImagePath = "";

    DatabaseHelper db;

    ActivityResultLauncher<String> galleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            // Show the selected image directly using URI
                            imagePreview.setImageURI(uri);
                            imagePreview.setVisibility(
                                    android.view.View.VISIBLE);

                            // Convert URI to Bitmap then save to file so the path can be stored in the database
                            try {
                                InputStream inputStream = getContentResolver()
                                        .openInputStream(uri);
                                Bitmap bitmap = BitmapFactory
                                        .decodeStream(inputStream);
                                selectedImagePath = saveBitmapToFile(bitmap);
                            } catch (IOException e) {
                                Toast.makeText(this,
                                        "Failed to save image.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Link UI elements
        radioGroupPostType = findViewById(R.id.radioGroupPostType);
        editName           = findViewById(R.id.editName);
        editPhone          = findViewById(R.id.editPhone);
        editDescription    = findViewById(R.id.editDescription);
        editLocation       = findViewById(R.id.editLocation);
        buttonPickDate     = findViewById(R.id.buttonPickDate);
        buttonPickImage    = findViewById(R.id.buttonPickImage);
        buttonSave         = findViewById(R.id.buttonSave);
        textSelectedDate   = findViewById(R.id.textSelectedDate);
        imagePreview       = findViewById(R.id.imagePreview);

        db = new DatabaseHelper(this);

        buttonPickDate.setOnClickListener(v -> showDatePicker());

        // Open the gallery
        buttonPickImage.setOnClickListener(v ->
                galleryLauncher.launch("image/*"));

        buttonSave.setOnClickListener(v -> saveItem());

        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
    }

    void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day   = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view,
                                          int selectedYear, int selectedMonth,
                                          int selectedDay) {
                        selectedDate = selectedYear + "-"
                                + String.format("%02d", selectedMonth + 1)
                                + "-"
                                + String.format("%02d", selectedDay);
                        textSelectedDate.setText(selectedDate);
                    }
                };

        new DatePickerDialog(this, dateListener,
                year, month, day).show();
    }

    // Saves the bitmap to the app's internal storage
    // Returns the file path as a String to store in the database
    String saveBitmapToFile(Bitmap bitmap) {
        File file = new File(getFilesDir(),
                "img_" + System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    void saveItem() {
        // Get post type from radio buttons
        int selectedRadioId = radioGroupPostType.getCheckedRadioButtonId();
        String postType = (selectedRadioId == R.id.radioLost)
                ? "Lost" : "Found";

        String name        = editName.getText().toString().trim();
        String phone       = editPhone.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String location    = editLocation.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            Toast.makeText(this,
                    "Item name is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this,
                    "Please pick a date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImagePath.isEmpty()) {
            Toast.makeText(this,
                    "Please upload an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate timestamp for when the post was created
        String timestamp = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date());

        // Build the item and insert into database
        LostFoundItem item = new LostFoundItem(
                0, postType, name, phone, description,
                selectedDate, location, selectedImagePath, timestamp);

        db.insertItem(item);

        Toast.makeText(this,
                "Item posted!", Toast.LENGTH_SHORT).show();
        finish();
    }
}