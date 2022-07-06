package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smartshoppinglist.R;

import java.util.List;

import core.AppDatabase;
import core.Item;
import core.ItemDao;
import core.ListSorter;
import math.Vector2;

public class EntryActivity extends AppCompatActivity {

    Button doButton;
    Button cancelButton;
    EditText nameField;
    EditText unitField;
    EditText eanField;

    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        doButton = findViewById(R.id.doButton);
        cancelButton = findViewById(R.id.cancel);
        nameField = findViewById(R.id.editItemName);
        unitField = findViewById(R.id.editItemUnit);
        eanField = findViewById(R.id.editItemEAN);

        Intent intent = getIntent();

        if (intent.hasExtra("itemID")) {
            int id = intent.getIntExtra("itemID", -1);
            if (id == -1) return;
            adjustUI();
            fillFields(id);
            doButton.setOnClickListener(v -> EntryActivity.this.updateItem());
        } else {
            doButton.setOnClickListener(v -> EntryActivity.this.createItem());
        }
        cancelButton.setOnClickListener(v1 -> onBackPressed());
    }

    private void createItem() {
        String name = nameField.getText().toString();
        String unit = unitField.getText().toString();
        String ean = eanField.getText().toString();

        Item item = new Item(name, ean, unit);

        ItemDao dao = AppDatabase.getDb(this).itemDao();
        AsyncTask.execute(() -> dao.insertItem(item));
        finish();
    }

    private void updateItem() {
        String name = nameField.getText().toString();
        String unit = unitField.getText().toString();
        String ean = eanField.getText().toString();

        item.setName(name);
        item.setUnit(unit);
        item.setEan(ean);

        ItemDao dao = AppDatabase.getDb(this).itemDao();
        AsyncTask.execute(() -> dao.updateItem(item));
        finish();
    }

    private void fillFields(int id) {

        ItemDao dao = AppDatabase.getDb(this).itemDao();

        AsyncTask.execute(() -> {
            item = dao.getItem(id);

            nameField.setText(item.getName());
            unitField.setText(item.getUnit());
            eanField.setText(item.getEan());
        });
    }

    private void adjustUI() {
        doButton.setText("Update");
    }
}