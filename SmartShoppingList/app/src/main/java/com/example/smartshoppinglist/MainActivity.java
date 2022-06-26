package com.example.smartshoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
	private Button btnRefresh;
	private Button btnAdd;
	private ListView listView;

	private ArrayList<Item> itemList;
	private ItemListAdapter itemAdapter;

	private int counter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRefresh = findViewById(R.id.btnRefresh);
		btnAdd = findViewById(R.id.btnAdd);

		itemList = new ArrayList<Item>();

		if (itemList.isEmpty()) {
			itemList.add(new Item("Test", new Vector2(0,0)));
		}

		RecyclerView listHolder = findViewById(R.id.itemListView);
		itemAdapter = new ItemListAdapter(itemList);
		listHolder.setAdapter(itemAdapter);
		listHolder.setLayoutManager(new LinearLayoutManager(this));

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
				Item item = new Item("#" + counter + " HalloWelt", Vector2.random(0f, 100f));
				System.out.println("Pos: " + item.getPosition().toString());
				counter++;
				itemAdapter.getEintraege_liste().add(item);
				itemAdapter.notifyDataSetChanged();
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