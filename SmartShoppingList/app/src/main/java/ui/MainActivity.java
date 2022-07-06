package ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import com.example.smartshoppinglist.R;

import java.util.List;

import core.Item;
import core.AppDatabase;
import core.ItemDao;
import core.ListSorter;
import math.Vector2;

public class MainActivity extends AppCompatActivity
{
	private Button btnRefresh;
	private Button btnAdd;
	private Button btnScan;
	private RecyclerView listHolder;

	private ItemListAdapter itemAdapter;

	private ActivityResultLauncher launchScanActivity;

	private int counter = 0;

	private List<Item> itemList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRefresh = findViewById(R.id.btnRefresh);
		btnAdd = findViewById(R.id.btnAdd);
		btnScan = findViewById(R.id.btnScan);

		listHolder = findViewById(R.id.itemListView);

		AppDatabase db = AppDatabase.getDb(this);
				LiveData<List<Item>> data = db.itemDao().getAll();
		data.observe(this, items -> {
			items = ListSorter.sortGenetic(items, 500, 100);
			if (items.isEmpty()) {
				items.add(new Item("Test", new Vector2(0,0)));
			}
			itemAdapter.submitList(items);
			itemList = items;
		});

		itemAdapter = new ItemListAdapter();
		listHolder.setAdapter(itemAdapter);
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
							Item item = (Item) bundle.getParcelable(ScannerActivity.RETURN_VALUE);

							if(item == null)
							{
								System.out.println("Scanner returned null");
								return;
							}
							System.out.println(item.toString());
							addItemToList(item);
						}
					}
				}
		);

		initBtns();
	}

	private void initBtns()
	{
		if(btnAdd == null || btnRefresh == null)
		{
			return;
		}

		btnAdd.setOnClickListener(view -> {
			Intent open = new Intent(this, EntryActivity.class);
			startActivity(open);
		});

		btnScan.setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
			launchScanActivity.launch(intent);
		});

		btnRefresh.setOnClickListener(view -> {
			itemList = ListSorter.sortGenetic(itemList, 500, 100);
			itemAdapter.submitList(itemList);
		});
	}

	private void addItemToList(Item item)
	{
		ItemDao dao = AppDatabase.getDb(this).itemDao();
		AsyncTask.execute(() -> dao.insertItem(item));
	}
}