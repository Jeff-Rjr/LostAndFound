package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lostandfound.adapters.ItemAdapter;
import com.example.lostandfound.database.DatabaseHelper;
import com.example.lostandfound.database.LostFoundItem;
import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    RecyclerView recyclerItems;
    EditText editSearch;
    Spinner spinnerFilter;
    ItemAdapter adapter;
    DatabaseHelper db;
    String selectedFilter = "All";
    String[] filters = {"All", "Lost", "Found"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        recyclerItems = findViewById(R.id.recyclerItems);
        editSearch    = findViewById(R.id.editSearch);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        db = new DatabaseHelper(this);

        // Set up filter spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, filters);
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);

        spinnerFilter.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            android.view.View view, int position, long id) {
                        selectedFilter = filters[position];
                        loadItems();
                    }
                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {}
                });

        recyclerItems.setLayoutManager(new LinearLayoutManager(this));

        // Tap an item to open the detail screen
        adapter = new ItemAdapter(new ArrayList<>(), item -> {
            Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra("itemId",          item.id);
            intent.putExtra("postType",        item.postType);
            intent.putExtra("name",            item.name);
            intent.putExtra("phone",           item.phone);
            intent.putExtra("description",     item.description);
            intent.putExtra("date",            item.date);
            intent.putExtra("location",        item.location);
            intent.putExtra("imagePath",       item.imagePath);
            intent.putExtra("timestamp",       item.timestamp);
            startActivity(intent);
        });

        recyclerItems.setAdapter(adapter);
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());

        // Search bar
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                searchItems(s.toString().trim());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    void loadItems() {
        List<LostFoundItem> items = db.getAllItems(selectedFilter);
        adapter.updateList(items);
    }

    void searchItems(String query) {
        List<LostFoundItem> allItems = db.getAllItems(selectedFilter);
        if (query.isEmpty()) {
            adapter.updateList(allItems);
            return;
        }
        List<LostFoundItem> filtered = new ArrayList<>();
        for (LostFoundItem item : allItems) {
            if (item.name.toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        adapter.updateList(filtered);
    }
}