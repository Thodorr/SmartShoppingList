package ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import core.AppDatabase;
import core.ItemDao;
import com.example.smartshoppinglist.R;

import core.Item;
import core.Product;
import core.ProductDao;

public class ItemListAdapter extends ListAdapter<Item, ItemListAdapter.ItemViewHolder> {

	protected ItemListAdapter( Boolean addUse) {
		super(new Item.DiffUtil());
		this.addUse = addUse;
	}

	private Context context;
	private Boolean addUse;

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		// TODO cast to ConstraintLayout not possible
		LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_list_layout, parent, false);
		ItemViewHolder vh = new ItemViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(ItemViewHolder holder, int position) {
		Item item = getItem(position);
		context = holder.itemView.getContext();
		holder.setItem(item);
	}

	public class ItemViewHolder extends RecyclerView.ViewHolder {
		public LinearLayout cl;
		private TextView tv;
		private ImageView icon;
		private Item item;

		public ItemViewHolder(LinearLayout v)
		{
			super(v);
			if (!addUse) {
				v.setOnClickListener(v1 -> {
					Intent open = new Intent(context, EntryActivity.class);
					open.putExtra("itemID", item.getItemID());
					context.startActivity(open);
				});

				v.setOnLongClickListener(v12 -> {
					ItemDao dao = AppDatabase.getDb(context).itemDao();
					AsyncTask.execute(() -> dao.deleteItem(item));
					return false;
				});
			} else {
				v.setOnClickListener(v1 -> {
					quickAdd(item);
				});
			}
			cl = v;
			icon = cl.findViewById(R.id.imageView2);
			tv = cl.findViewById(R.id.textView);
		}

		public void setItem(Item item) {
			this.item = item;
			tv.setText(item.getName());

			System.out.println("Icon: " + item);
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

	private void quickAdd(Item item) {
		Product product = new Product(item, 1, false);

		ProductDao dao = AppDatabase.getDb(context).productDao();
		AsyncTask.execute(() -> dao.insertProduct(product));

		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
}
