package ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartshoppinglist.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import core.AppDatabase;
import core.ListSorter;
import core.Product;
import core.ProductDao;

public class MainActivity extends AppCompatActivity
{
	FloatingActionButton btnAdd;
	private Button btnProducts;
	private TextView noItemsView;

	private ProductListAdapter productAdapter;

	private String currentSort = "dist";
	String message = "Sorted by distance";

	private List<Product> productList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle("Shopping List");

		btnAdd = findViewById(R.id.btnAdd);
		btnProducts = findViewById(R.id.btnProducts);
		noItemsView = findViewById(R.id.noItemView);
		noItemsView.setVisibility(View.GONE);

		RecyclerView listHolder = findViewById(R.id.itemListView);

		AppDatabase db = AppDatabase.getDb(this);
				LiveData<List<Product>> data = db.productDao().getAll();
		data.observe(this, products -> {
			products = (List<Product>) ListSorter.sortAlphabeticallyAscending(products);
			if (products.isEmpty()) {
				noItemsView.setVisibility(View.VISIBLE);
			}
			productAdapter.submitList(products);
			productList = products;
		});

		productAdapter = new ProductListAdapter();
		listHolder.setAdapter(productAdapter);
		listHolder.setLayoutManager(new LinearLayoutManager(this));

		initBtns();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_refresh, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.menuItemAlpha:
				productList = (List<Product>) ListSorter.sortAlphabeticallyAscending(productList);
				currentSort = "alpha";
				message = "Sorted alphabetically";
				break;
			case R.id.menuItemDist:
				productList = (List<Product>) ListSorter.sortGenetic(productList, 500, 100);
				currentSort = "dist";
				message = "Sorted by distance";
				break;
			case R.id.menuItemRefresh:
				System.out.println(currentSort);
				if (currentSort.equals("dist")) {
					productList = (List<Product>) ListSorter.sortGenetic(productList, 500, 100);
				} else {
					productList = (List<Product>) ListSorter.sortAlphabeticallyAscending(productList);
				}
				break;
			default:
				productList = (List<Product>) ListSorter.sortGenetic(productList, 500, 100);
				currentSort = "dist";
				message = "Sorted by distance";
				break;
		}
		productAdapter.submitList(productList);
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		return true;
	}

	private void initBtns()
	{
		if(btnAdd == null)
		{
			return;
		}

		btnAdd.setOnClickListener(view -> {
			Intent open = new Intent(this, AddMenuActivity.class);
			startActivity(open);
		});

		btnProducts.setOnClickListener(view -> {
			Intent open = new Intent(this, ProductActivity.class);
			startActivity(open);
			overridePendingTransition(R.anim.no_animation, R.anim.no_animation);
		});
	}

	private void addItemToList(Product product)
	{
		ProductDao dao = AppDatabase.getDb(this).productDao();
		AsyncTask.execute(() -> dao.insertProduct(product));
	}
}