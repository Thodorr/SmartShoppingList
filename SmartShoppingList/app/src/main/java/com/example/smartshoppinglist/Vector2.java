package com.example.smartshoppinglist;

public class Vector2
{
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
}
