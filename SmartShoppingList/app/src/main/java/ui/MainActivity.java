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

import java.util.List;

import core.IProduct;
import core.Item;
import core.AppDatabase;
import core.ItemDao;
import core.ListSorter;
import core.Product;
import core.ProductDao;
import math.Vector2;

public class MainActivity extends AppCompatActivity
{
	private Button btnRefresh;
	private Button btnAdd;
	private Button btnScan;
	private  Button btnProducts;
	private RecyclerView listHolder;

	private ProductListAdapter productAdapter;

	private ActivityResultLauncher launchScanActivity;

	private int counter = 0;

	private List<Product> productList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle("Shopping List");

		btnRefresh = findViewById(R.id.btnRefresh);
		btnAdd = findViewById(R.id.btnAdd);
		btnScan = findViewById(R.id.btnScan);
		btnProducts = findViewById(R.id.btnProducts);

		listHolder = findViewById(R.id.itemListView);

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

		launchScanActivity = registerForActivityResult(
				new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>()
				{
					@Override
					public void onActivityResult(ActivityResult result)
					{
						if(result.getResultCode() == Activity.RESULT_OK)
						{
							Intent data = result.getData();
							if(data == null || !data.hasExtra(ScannerActivity.RETURN_VALUE))
							{
								return;
							}

							// TODO: handle scanner result data
							Bundle bundle = data.getExtras();
							Product product = (Product) bundle.getParcelable(ScannerActivity.RETURN_VALUE);

							if(product == null)
							{
								System.out.println("Scanner returned null");
								return;
							}
							System.out.println(product.toString());
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
				productAdapter.submitList(productList);
				//Toast.makeText(this, "Sorted A - Z", Toast.LENGTH_SHORT).show();
				break;
			case R.id.menuItemDist:
			case R.id.menuItemRefresh:
			default:
				productList = (List<Product>) ListSorter.sortGenetic(productList, 500, 100);
				productAdapter.submitList(productList);
				//Toast.makeText(this, "Sorted by distance", Toast.LENGTH_SHORT).show();
		}

		return true;
	}

	private void initBtns()
	{
		if(btnAdd == null || btnRefresh == null)
		{
			return;
		}

		btnAdd.setOnClickListener(view -> {
			Intent open = new Intent(this, AddMenuActivity.class);
			startActivity(open);
		});

		btnScan.setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
			launchScanActivity.launch(intent);
		});

		btnRefresh.setOnClickListener(view -> {
			productList = (List<Product>) ListSorter.sortGenetic(productList, 500, 100);
			productAdapter.submitList(productList);
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