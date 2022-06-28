package core;

import android.os.Parcel;
import android.os.Parcelable;

import math.Vector2;

public class Item implements Parcelable
{
	private String name;
	private Vector2 pos;

	private boolean checked;

	public Item(String name, float posX, float posY)
	{
		this.name = name;
		pos = new Vector2(posX, posY);
	}

	public Item(String name, Vector2 position)
	{
		this.name = name;
		this.pos = position;
	}

	public Item(String name)
	{
		this.name = name;
		this.pos = Vector2.random(0f, 100f);
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
		return pos;
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

	@Override
	public String toString()
	{
		return "Item{" +
				"name='" + name + '\'' +
				", pos=" + pos +
				", checked=" + checked +
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
		parcel.writeFloat(pos.x);
		parcel.writeFloat(pos.y);
	}
}
