package com.example.smartshoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<Item>
{

	public ItemListAdapter(@NonNull Context context, ArrayList<Item> list)
	{
		super(context, 0, list);
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent)
	{
		View elem = null;

		if(elem == null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			elem = inflater.inflate(R.layout.item_list_layout, null);
		}

		Item item = getItem(pos);

		Switch switchView = (Switch) elem.findViewById(R.id.switchView);
		switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton btn, boolean isChecked)
			{
				item.setChecked(isChecked);
			}
		});
		switchView.setChecked(item.isChecked());

		TextView textView = (TextView) elem.findViewById(R.id.textView);
		textView.setText(getItem(pos).getName());

		return elem;
	}
}
