package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ListSorter
{
	public static List<? extends IProduct> sortGenetic(List<? extends IProduct> items, int populationSize, int maxGenerations)
	{
		if (items == null) return null;
		if (items.isEmpty()) return items;
		System.out.println("Sorting started");

		IProduct[] is = new IProduct[items.size()];
		is = items.toArray(is);
		List<Integer> order = new ArrayList<>();
		for (int i = 0; i < is.length; i++)
		{
			order.add(i);
		}

		GeneticAlgorithm genetic = new GeneticAlgorithm(is, populationSize);

		List<Integer> orderCopy = new ArrayList<>(order.size());
		for (Integer i : order)
		{
			orderCopy.add(i);
		}

		for (int i = 0; i < populationSize; i++)
		{
			Collections.shuffle(orderCopy);
			genetic.getPopulation().add(orderCopy);
		}

		// run genetic algorithm
		for(int i = 0; i < maxGenerations; i++)
		{
			genetic.runOnce();
		}

		List<Integer> bestEver = genetic.getBestEver();
		ArrayList<IProduct> sorted = new ArrayList<>(items.size());
		for (int i = 0; i < bestEver.size(); i++)
		{
			int n = bestEver.get(i);
			sorted.add(items.get(n));
		}

		System.out.println("Sorting finished");
		return sorted;
	}

	public static void Test(IProduct iProduct)
	{

	}

	public static <T extends IProduct> List<T> sortAlphabeticallyAscending(List<T> items)
	{
		List<T> newList = new ArrayList<T>(items);
		newList.sort(Comparator.comparing(o -> o.getItem().getName()));
		return newList;
	}
}
