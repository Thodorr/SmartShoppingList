package ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.smartshoppinglist.R;

import java.util.List;

import core.AppDatabase;
import core.Item;
import core.ItemDao;
import core.Product;
import core.ProductDao;
import core.Supermarket;
import math.Vector2;

public class EntryActivity extends AppCompatActivity
{

	Button doButton;
	Button cancelButton;
	EditText nameField;
	EditText unitField;
	EditText eanField;
	EditText shelfField;
	Spinner iconSpinner;

	Item item;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry);

		setTitle("Add Item");

		doButton = findViewById(R.id.doButton);
		cancelButton = findViewById(R.id.cancel);
		nameField = findViewById(R.id.editItemName);
		unitField = findViewById(R.id.editItemUnit);
		eanField = findViewById(R.id.editItemEAN);
		shelfField = findViewById(R.id.editItemShelf);
		iconSpinner = findViewById(R.id.iconSpinner);

		Intent intent = getIntent();

		if (intent.hasExtra("itemID"))
		{
			int id = intent.getIntExtra("itemID", -1);
            if (id == -1)
            {
                return;
            }
			adjustUI();
			fillFields(id);
			doButton.setOnClickListener(v -> EntryActivity.this.updateItem());
		}
		else
		{
			doButton.setOnClickListener(v -> EntryActivity.this.createItem());
		}

		if (intent.hasExtra("ean")) {
			String ean = intent.getStringExtra("ean");
			eanField.setText(ean);
		}
		cancelButton.setOnClickListener(v1 -> onBackPressed());
	}

	private void createItem()
	{
		String name = nameField.getText().toString();
		String unit = unitField.getText().toString();
		String ean = eanField.getText().toString();
		String iconName = iconSpinner.getSelectedItem().toString();
		int shelf = (int) Integer.parseInt(shelfField.getText().toString());

		Item item = new Item(name, new Supermarket().getPosition(shelf), ean, unit, iconName);

		if (getIntent().hasExtra("direct")) {
			Product product = new Product(item, 100, false);

			ProductDao dao = AppDatabase.getDb(this).productDao();
			AsyncTask.execute(() -> dao.insertProduct(product));

			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else {
			ItemDao dao = AppDatabase.getDb(this).itemDao();
			AsyncTask.execute(() -> dao.insertItem(item));

			Intent intent = new Intent(this, ProductActivity.class);
			startActivity(intent);
		}

	}

	private void updateItem()
	{
		String name = nameField.getText().toString();
		String unit = unitField.getText().toString();
		String ean = eanField.getText().toString();
		int shelf = (int) Integer.parseInt(shelfField.getText().toString());
		String iconName = iconSpinner.getSelectedItem().toString();

		item.setName(name);
		item.setUnit(unit);
		item.setEan(ean);
		item.setPosition(new Supermarket().getPosition(shelf));
		item.setIconName(iconName);

		ItemDao dao = AppDatabase.getDb(this).itemDao();
		AsyncTask.execute(() -> dao.updateItem(item));
		finish();
	}

	private void fillFields(int id)
	{

		ItemDao dao = AppDatabase.getDb(this).itemDao();

		AsyncTask.execute(() -> {
			item = dao.getItem(id);

			if (item == null)
			{
				return;
			}
			int shelf = new Supermarket().getShelfNr(item.getPosition());

			nameField.setText(item.getName());
			unitField.setText(item.getUnit());
			eanField.setText(item.getEan());
			shelfField.setText(Integer.toString(shelf));
			iconSpinner.setSelection(getIconIndex(item.getIconName()));
		});
	}

	private void adjustUI()
	{
		doButton.setText("Update");
	}

	private int getIconIndex(String iconName) {
		String[] iconNameList = getResources().getStringArray(R.array.iconNames);

		int counter = 0;
		for (String i : iconNameList) {
			if (i.equals(iconName)) {
				return counter;
			}
			counter++;
		}
		return -1;
	}
}