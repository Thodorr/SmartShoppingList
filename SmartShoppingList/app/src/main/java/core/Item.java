package core;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

import math.Vector2;

@Entity
public class Item implements Parcelable, IProduct
{
	@PrimaryKey(autoGenerate = true)
	private int itemID;
	private String name;
	private String unit;
	private String ean;
	private String iconName;
	private Vector2 position;

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	@Ignore
	public Item(String name, float posX, float posY)
	{
		this(name, new Vector2(posX, posY));
	}

	@Ignore
	public Item(String name, Vector2 position)
	{
		this(name, position, "", "", "");
	}

	@Ignore
	public Item(String name)
	{
		this(name, Vector2.random(0f, 100f));
	}

	public Item(String name, Vector2 position, String ean, String unit, String iconName) {
		this.name = name;
		this.position = position;
		this.ean = ean;
		this.unit = unit;
		this.iconName = iconName;
	}

	protected Item(Parcel in)
	{
		this(in.readString(), in.readFloat(), in.readFloat());
	}

	public static final Creator<Item> CREATOR = new Creator<Item>() {
		@Override
		public Item createFromParcel(Parcel in)
		{
			return new Item(in);
		}

		@Override
		public Item[] newArray(int size)
		{
			return new Item[size];
		}
	};

	public Vector2 getPosition()
	{
		return position;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}


	public int getItemID()
	{
		return itemID;
	}

	public void setItemID(int itemID)
	{
		this.itemID = itemID;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	@Override
	public String toString()
	{
		return "Item{" +
				"name='" + name + '\'' +
				", pos=" + position +
				", unit=" + unit +
				", ean=" + ean +
				", iconName=" + iconName +
				'}';
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeString(name);
		parcel.writeFloat(position.x);
		parcel.writeFloat(position.y);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Item item = (Item) o;
		return itemID == item.itemID && name.equals(item.name) && position.equals(item.position)
				&& ean.equals(item.ean) && item.iconName == iconName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(itemID, name, position, ean, iconName);
	}

	public void setId(int id) {
		this.itemID = id;
	}

	@Override
	public Item getItem()
	{
		return this;
	}

	public static class DiffUtil extends androidx.recyclerview.widget.DiffUtil.ItemCallback<Item> {
		@Override
		public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
			return areContentsTheSame(oldItem, newItem);
		}

		@Override
		public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
			return oldItem.equals(newItem);
		}
	}
}
