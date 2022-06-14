package com.example.smartshoppinglist;

public class Vector2
{
	public static final Vector2 zero = new Vector2(0f, 0f);

	public float x;
	public float y;

	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public float distance(Vector2 a, Vector2 b)
	{
		return MathUtility.dist(a.x, a.y, b.x, b.y);
	}

	/***
	 * returns a Vector2 with random x and y coordinate between min and max.
	 * @param min inclusive
	 * @param max inclusive
	 * @return
	 */
	public static Vector2 random(float min, float max)
	{
		return new Vector2(MathUtility.randomRange(min, max), MathUtility.randomRange(min, max));
	}

	@Override
	public String toString()
	{
		return "Vector2{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
