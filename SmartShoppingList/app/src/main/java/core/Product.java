package core;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "product")
public class Product implements Parcelable, IProduct
{
	@PrimaryKey(autoGenerate = true)
	private int productID;
	@Embedded
	private Item item;

	private int amount;
	private boolean checked;

	public Product(Item item, int amount, boolean checked)
	{
		this.item = item;
		this.amount = amount;
		this.checked = checked;
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
	@Ignore
	protected Product(Parcel in)
	{
		this(in.readParcelable(Item.class.getClassLoader()), in.readInt(), in.readBoolean());
	}

	public Item getItem(){ return item; }

	public int getProductID()
	{
		return productID;
	}

	public void setProductID(int productID)
	{
		this.productID = productID;
	}

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public static final Creator<Product> CREATOR = new Creator<Product>()
	{
		@RequiresApi(api = Build.VERSION_CODES.Q)
		@Override
		public Product createFromParcel(Parcel in)
		{
			return new Product(in);
		}

		@Override
		public Product[] newArray(int size)
		{
			return new Product[size];
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeParcelable(item, i);
		parcel.writeInt(amount);
		parcel.writeBoolean(checked);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return productID == product.productID && item.equals(product.getItem())
				&& product.checked == checked && product.amount == amount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(productID, item);
	}

	@Override
	public String toString() {
		return item.getName() + ": " +item.getPosition();
	}

	public static class DiffUtil extends androidx.recyclerview.widget.DiffUtil.ItemCallback<Product> {
		@Override
		public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
			return areContentsTheSame(oldItem, newItem);
		}

		@Override
		public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
			return oldItem.equals(newItem);
		}
	}
}
