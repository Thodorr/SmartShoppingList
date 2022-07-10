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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshoppinglist.R;

import core.AppDatabase;
import core.Item;
import core.ItemDao;
import core.Product;
import core.ProductDao;

public class ProductListAdapter extends ListAdapter<Product, ProductListAdapter.ProductViewHolder>
{
	protected ProductListAdapter() {
		super(new Product.DiffUtil());
	}

	private Context context;

	@NonNull
	@Override
	public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_list_layout, parent, false);
		ProductViewHolder vh = new ProductViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(@NonNull ProductViewHolder holder, int position)
	{
		Product product = getItem(position);
		context = holder.itemView.getContext();
		holder.setProduct(product);
	}

	public class ProductViewHolder extends RecyclerView.ViewHolder {
		public LinearLayout cl;
		private TextView tv;
		private Switch sw;
		private ImageView icon;
		private Product product;

		public ProductViewHolder(LinearLayout v)
		{
			super(v);
			v.setOnClickListener(v1 -> {
				Intent open = new Intent(context, EntryActivity.class); // TODO: Extra should be Product instead of Item
				open.putExtra("productID", product.getProductID());
				context.startActivity(open);
			});
			v.setOnLongClickListener(v12 -> {
				ProductDao dao = AppDatabase.getDb(context).productDao();
				AsyncTask.execute(() -> dao.deleteProduct(product));
				return false;
			});
			cl = v;
			icon = cl.findViewById(R.id.imageView2);
			tv = cl.findViewById(R.id.textView);
			sw = cl.findViewById(R.id.switchView);

			sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
				product.setChecked(isChecked);
			});

		}

		public void setProduct(Product product) {
			this.product = product;
			tv.setText(product.getItem().getName());

			switch(product.getItem().getIconName()) {
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
