package math;

import java.util.ArrayList;
import java.util.List;

public abstract class MathUtility
{
	/***
	 * Returns a float within min (inclusive) and max (inclusive).
	 * If min > max the values are automatically swapped.
	 * @param min
	 * @param max
	 * @return
	 */
	public static float randomRange(float min, float max)
	{
		if(min > max)
		{
			float tmp = min;
			min = max;
			max = min;
		}

		float range = max - min;
		float random = (float) Math.floor(Math.random() * range);

		return random;
	}

	public static List<Integer> copyList(List<Integer> list)
	{
		List<Integer> copiedList = new ArrayList<Integer>(list.size());
		for (Integer i : list)
		{
			copiedList.add(i);
		}

		return copiedList;
	}

	public static void swap(List<Integer> a, int i, int j)
	{
		int temp = a.get(i);
		a.set(i, a.get(j));
		a.set(j, temp);
	}

	public static float dist(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	public static float dist(Vector2 a, Vector2 b)
	{
		return dist(a.x, a.y, b.x, b.y);
	}
}
