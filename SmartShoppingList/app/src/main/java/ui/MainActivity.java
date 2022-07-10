package ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
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

	private ProductListAdapter productAdapter;

	private final int counter = 0;
	private final String currentSort = "distance";

	private List<Product> productList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle("Shopping List");

		btnAdd = findViewById(R.id.btnAdd);
		btnProducts = findViewById(R.id.btnProducts);

		RecyclerView listHolder = findViewById(R.id.itemListView);

		AppDatabase db = AppDatabase.getDb(this);
				LiveData<List<Product>> data = db.productDao().getAll();
		data.observe(this, products -> {
			products = (List<Product>) ListSorter.sortGenetic(products, 500, 100);
			if (products.isEmpty()) {
				// TODO: ist hier ein Default Objekt nötig?
			}
			productAdapter.submitList(products);
			productList = products;
		});

		productAdapter = new ProductListAdapter();
		listHolder.setAdapter(productAdapter);
		listHolder.setLayoutManager(new LinearLayoutManager(this));

		// TODO: handle scanner result data
		ActivityResultLauncher launchScanActivity = registerForActivityResult(
				new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
					@Override
					public void onActivityResult(ActivityResult result) {
						if (result.getResultCode() == Activity.RESULT_OK) {
							Intent data = result.getData();
							if (data == null || !data.hasExtra(ScannerActivity.RETURN_VALUE)) {
								return;
							}

							// TODO: handle scanner result data
							Bundle bundle = data.getExtras();
							Product product = (Product) bundle.getParcelable(ScannerActivity.RETURN_VALUE);

							if (product == null) {
								System.out.println("Scanner returned null");
								return;
							}
							System.out.println(product);
							addItemToList(product);
						}
					}
				}
		);

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
				System.out.println(productList);
				productAdapter.submitList(productList);
				Toast.makeText(this, "Sorted alphabetically", Toast.LENGTH_SHORT).show();
				break;
			case R.id.menuItemDist:
			case R.id.menuItemRefresh:
			default:
				productList = (List<Product>) ListSorter.sortGenetic(productList, 500, 100);
				System.out.println(productList);
				productAdapter.submitList(productList);
				Toast.makeText(this, "Sorted by distance", Toast.LENGTH_SHORT).show();
				break;
		}

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
		});
	}

	private void addItemToList(Product product)
	{
		ProductDao dao = AppDatabase.getDb(this).productDao();
		AsyncTask.execute(() -> dao.insertProduct(product));
	}
}