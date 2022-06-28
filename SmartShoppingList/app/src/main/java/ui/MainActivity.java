package ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.smartshoppinglist.R;

import java.util.ArrayList;

import core.Item;
import math.Vector2;
import ui.ItemListAdapter;

public class MainActivity extends AppCompatActivity
{
	private Button btnRefresh;
	private Button btnAdd;
	private Button btnScan;
	private ListView listView;

	private ArrayList<Item> itemList;
	private ItemListAdapter itemAdapter;

	private ActivityResultLauncher launchScanActivity;

	private int counter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRefresh = findViewById(R.id.btnRefresh);
		btnAdd = findViewById(R.id.btnAdd);
		btnScan = findViewById(R.id.btnScan);

		itemList = new ArrayList<Item>();

		if (itemList.isEmpty()) {
			itemList.add(new Item("Test", new Vector2(0,0)));
		}

		RecyclerView listHolder = findViewById(R.id.itemListView);
		itemAdapter = new ItemListAdapter(itemList);
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
							if(data == null)
							{
								System.out.println("Scanner returned null");
								return;
							}
							// TODO: handle scanner result data
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

		btnAdd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Item item = new Item("#" + counter + " HalloWelt");
				System.out.println("Pos: " + item.getPosition().toString());
				counter++;
				itemAdapter.getEintraege_liste().add(item);
				itemAdapter.notifyDataSetChanged();
			}
		});

		btnScan.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
				launchScanActivity.launch(intent);
			}
		});

		btnRefresh.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				itemAdapter.refreshList();
			}
		});
	}
}