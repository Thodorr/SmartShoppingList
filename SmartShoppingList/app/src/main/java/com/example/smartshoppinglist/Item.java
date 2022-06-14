package com.example.smartshoppinglist;

public class Item
{
	private String name;
	private Vector2 pos;

	private boolean checked;

	public Item(String name, int posX, int posY)
	{
		this.name = name;
		pos = new Vector2(posX, posY);
	}

	public Item(String name, Vector2 position)
	{
		this.name = name;
		this.pos = position;
	}

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
}
