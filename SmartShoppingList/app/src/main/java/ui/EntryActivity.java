package ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.smartshoppinglist.R;

import core.AppDatabase;
import core.Item;
import core.ItemDao;
import core.Product;
import core.ProductDao;
import math.Vector2;

public class EntryActivity extends AppCompatActivity
{

	Button doButton;
	Button cancelButton;
	EditText nameField;
	EditText unitField;
	EditText eanField;
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

		Item item = new Item(name, Vector2.random(0,100f), ean, unit, iconName);

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

		item.setName(name);
		item.setUnit(unit);
		item.setEan(ean);

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

			nameField.setText(item.getName());
			unitField.setText(item.getUnit());
			eanField.setText(item.getEan());
		});
	}

	private void adjustUI()
	{
		doButton.setText("Update");
	}
}