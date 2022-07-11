package ui;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshoppinglist.R;

import core.AppDatabase;
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
		ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_list_layout, parent, false);
		return new ProductViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ProductViewHolder holder, int position)
	{
		Product product = getItem(position);
		context = holder.itemView.getContext();
		holder.setProduct(product);
	}

	public class ProductViewHolder extends RecyclerView.ViewHolder {
		public ConstraintLayout cl;
		private final TextView tv;
		private final ImageView icon;
		private final TextView amountView;
		private final TextView unitView;
		private Product product;
		private View moreButton;


		public ProductViewHolder(ConstraintLayout v)
		{
			super(v);
			v.setOnLongClickListener(v12 -> {
				ProductDao dao = AppDatabase.getDb(context).productDao();
				AsyncTask.execute(() -> dao.deleteProduct(product));
				return false;
			});
			cl = v;
			icon = cl.findViewById(R.id.imageView2);
			tv = cl.findViewById(R.id.nameView);
			amountView = cl.findViewById(R.id.amountView);
			unitView = cl.findViewById(R.id.unitView);
			moreButton = cl.findViewById(R.id.moreButton);
		}

		public void setProduct(Product product) {
			this.product = product;
			tv.setText(product.getItem().getName());
			unitView.setText(product.getItem().getUnit());
			amountView.setText(Integer.toString(product.getAmount()));
			moreButton.setOnClickListener(v -> {
				System.out.println("test");
				product.setAmount(product.getAmount() + 1);
				ProductDao dao = AppDatabase.getDb(context).productDao();
				AsyncTask.execute(() -> dao.updateProduct(product));
			});

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
