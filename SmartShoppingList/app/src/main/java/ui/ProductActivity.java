package ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshoppinglist.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import core.AppDatabase;
import core.DatabaseFiller;
import core.Item;
import core.ListSorter;

public class ProductActivity extends AppCompatActivity
{
	static public String RETURN_VALUE = "products";

	private RecyclerView listHolder;
	private FloatingActionButton addProduct;
	private Button shoppingListButton;


	private int counter = 0;

	private Context context;

	private List<Item> itemList;
	private ItemListAdapter itemAdapter;

	private List<Item> selectedProducts;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);

		setTitle("Product List");

		context = this;

		selectedProducts = new ArrayList<Item>();

		listHolder = findViewById(R.id.itemListView);
		addProduct = findViewById(R.id.addProductButton);
		shoppingListButton = findViewById(R.id.btnList);

		AppDatabase db = AppDatabase.getDb(this);
		LiveData<List<Item>> data = db.itemDao().getAll();
		data.observe(this, items -> {
			items = (List<Item>) ListSorter.sortAlphabeticallyAscending(items);
			if (items.isEmpty()) {
				DatabaseFiller filler = new DatabaseFiller();
				filler.fillDatabase(context);
			}
			itemAdapter.submitList(items);
			itemList = items;
		});

		Boolean addUsage = (getIntent().hasExtra("add"));
		itemAdapter = new ItemListAdapter(addUsage);
		listHolder.setAdapter(itemAdapter);
		listHolder.setLayoutManager(new GridLayoutManager(this, 2));

		if (!addUsage) {
			addProduct.setOnClickListener(v -> {
				Intent open = new Intent(this, EntryActivity.class);
				startActivity(open);
			});
		} else {
			addProduct.setImageResource(R.drawable.ic_baseline_check_24);
			addProduct.setOnClickListener(v -> onBackPressed());
		}

		shoppingListButton.setOnClickListener(v -> {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.no_animation, R.anim.no_animation);
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_search, menu);

		SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				AppDatabase db = AppDatabase.getDb(context);
				AsyncTask.execute(() -> {
					List<Item> searchResults = db.itemDao().getBySearch(newText);
					itemAdapter.submitList(searchResults);
				});
				return false;
			}
		});

		return true;
	}

	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}