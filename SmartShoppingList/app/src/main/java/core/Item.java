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
public class Item implements Parcelable
{
	@PrimaryKey(autoGenerate = true)
	private int id;
	private String name;
	private int amount;
	private String unit;
	private String ean;


	private boolean checked;

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	private Vector2 position;


	@Ignore
	public Item(String name, float posX, float posY)
	{
		this.name = name;
		position = new Vector2(posX, posY);
	}

	@Ignore
	public Item(String name, Vector2 position)
	{
		this.name = name;
		this.position = position;
	}

	@Ignore
	public Item(String name)
	{
		this.name = name;
		this.position = Vector2.random(0f, 100f);
	}

	public Item(String name, String ean, String unit) {
		this.name = name;
		this.position = Vector2.random(0f, 100f);
		this.ean = ean;
		this.amount = 0;
		this.unit = unit;
	}

	protected Item(Parcel in)
	{
		this(in.readString(), in.readFloat(), in.readFloat());
	}

	String getText() {
		if (isChecked()) {
			return getName() + " (erledigt)";
		} else {
			return getName();
		}
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

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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

	@Override
	public String toString()
	{
		return "Item{" +
				"name='" + name + '\'' +
				", pos=" + position +
				", checked=" + checked +
				", amount=" + amount +
				", unit=" + unit +
				", ean=" + ean +
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
		return id == item.id && checked == item.checked && name.equals(item.name) && position.equals(item.position)
				&& ean.equals(item.ean);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, checked, position, ean);
	}

	public void setId(int id) {
		this.id = id;
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
