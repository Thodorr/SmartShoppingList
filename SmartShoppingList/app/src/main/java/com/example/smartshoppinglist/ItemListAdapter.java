package com.example.smartshoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder>
{
	private ArrayList<Item> eintraege_liste;
	private boolean on_bind = false;

	public ItemListAdapter(ArrayList<Item> eintraege_liste)
	{
		super();
		this.eintraege_liste = eintraege_liste;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		// create a new view
		// TODO cast to ConstraintLayout not possible
		LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_list_layout, parent, false);
		MyViewHolder vh = new MyViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position)
	{
		on_bind = true;
		holder.getTextView().setText(eintraege_liste.get(position).getName());
		holder.getSwitchview().setChecked(eintraege_liste.get(position).isChecked());
		on_bind = false;
	}

	@Override
	public int getItemCount()
	{
		return eintraege_liste.size();
	}

	public ArrayList<Item> getEintraege_liste()
	{
		return eintraege_liste;
	}

	public void refreshList()
	{
		eintraege_liste = ListSorter.sortGenetic(eintraege_liste, 500, 100);
		notifyDataSetChanged();
	}

	public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener
	{

		public LinearLayout cl;
		public Switch switchv;
		public TextView tv;

		public MyViewHolder(LinearLayout v)
		{
			super(v);
			v.setOnClickListener(this);
			v.setOnLongClickListener(this);
			cl = v;
			switchv = cl.findViewById(R.id.switchView);
			switchv.setOnCheckedChangeListener(this);
			tv = cl.findViewById(R.id.textView);
		}

		public Switch getSwitchview()
		{
			return switchv;
		}

		public TextView getTextView()
		{
			return tv;
		}

		@Override
		public void onClick(View view)
		{

		}

		@Override
		public boolean onLongClick(View view)
		{
			/*
			int position = getAdapterPosition();
			eintraege_liste.remove(eintraege_liste.get(position));
			notifyItemRemoved(position);
			*/
			return false;
		}

		@Override
		public void onCheckedChanged(CompoundButton compoundButton, boolean b)
		{
			int position = getAdapterPosition();
			if (!b && eintraege_liste.get(position).isChecked()) {
				eintraege_liste.get(position).setName(eintraege_liste.get(position).getName() + " (erledigt)");
				eintraege_liste.get(position).setChecked(false);
			}
			if (b && !eintraege_liste.get(getAdapterPosition()).isChecked()) {
				eintraege_liste.get(position).setName(eintraege_liste.get(position).getName().replaceAll(" \\(erledigt\\)", "").toString());
				eintraege_liste.get(position).setChecked(true);
			}
			if (!on_bind) notifyItemChanged(position);
		}
	}
}
