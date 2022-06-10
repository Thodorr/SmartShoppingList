package com.example.smartshoppinglist;

public class Item
{
	private Vector2 pos;

	public Item(int posX, int posY)
	{
		pos = new Vector2(posX, posY);
	}

	public Item(Vector2 position)
	{
		this.pos = position;
	}

	public Vector2 getPosition()
	{
		return pos;
	}
}
