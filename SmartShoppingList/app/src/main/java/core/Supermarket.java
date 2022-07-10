package core;

import math.Vector2;

public class Supermarket
{
	private int width = 3;
	private int height = 3;

	private Vector2[] shelfs;

	public Supermarket()
	{
		shelfs = new Vector2[width * height];

		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				shelfs[width * y + x] = new Vector2(x, y);
			}
		}
	}

	public Vector2 getPosition(int shelfNr)
	{
		// shelfNr = width * row + col
		return shelfs[shelfNr];
	}

	public int getShelfNr(Vector2 pos)
	{
		if((pos.x >= width || pos.x < 0) && (pos.y >= height || pos.y < 0))
		{
			return -1;
		}

		return (int) width * (int)pos.y + (int)pos.x;
	}
}
