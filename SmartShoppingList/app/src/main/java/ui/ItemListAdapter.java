package ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import core.AppDatabase;
import core.ItemDao;
import com.example.smartshoppinglist.R;

import core.Item;

public class ItemListAdapter extends ListAdapter<Item, ItemListAdapter.MyViewHolder> {

	protected ItemListAdapter() {
		super(new Item.DiffUtil());
	}

	private Context context;

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		// TODO cast to ConstraintLayout not possible
		LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_list_layout, parent, false);
		MyViewHolder vh = new MyViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Item item = getItem(position);
		context = holder.itemView.getContext();
		holder.setItem(item);
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {
		public LinearLayout cl;
		private Switch switchv;
		private TextView tv;
		private ImageView icon;
		private Item item;

		public MyViewHolder(LinearLayout v)
		{
			super(v);
			v.setOnClickListener(v1 -> {
				Intent open = new Intent(context, EntryActivity.class);
				open.putExtra("itemID", item.getId());
				context.startActivity(open);
			});
			v.setOnLongClickListener(v12 -> {
				ItemDao dao = AppDatabase.getDb(context).itemDao();
				AsyncTask.execute(() -> dao.deleteItem(item));
				return false;
			});
			cl = v;
			switchv = cl.findViewById(R.id.switchView);
			icon = cl.findViewById(R.id.imageView2);
			switchv.setOnCheckedChangeListener((buttonView, isChecked) -> {
				item.setChecked(isChecked);
			});
			tv = cl.findViewById(R.id.textView);
		}

		public void setItem(Item item) {
			this.item = item;
			tv.setText(item.getName());
			switchv.setChecked(item.isChecked());

			switch(item.getIconName()) {
				case "Croissant":
					icon.setImageResource(R.drawable.baseline_bakery_dining_24);
					break;
				case "Bread":
					icon.setImageResource(R.drawable.baseline_breakfast_dining_24);
					break;
				case "Burger":
					icon.setImageResource(R.drawable.baseline_lunch_dining_24);
					break;
				case "Egg":
					icon.setImageResource(R.drawable.baseline_egg_24);
					break;
				case "Meat":
					icon.setImageResource(R.drawable.baseline_kebab_dining_24);
					break;
				case "Coffee":
					icon.setImageResource(R.drawable.baseline_local_cafe_24);
					break;
				case "Pizza":
					icon.setImageResource(R.drawable.baseline_local_pizza_24);
					break;
				case "Ice Cream":
					icon.setImageResource(R.drawable.baseline_icecream_24);
					break;
				default:
					break;
			}
		}
	}
}
