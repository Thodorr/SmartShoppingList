package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.GeneticAlgorithm;
import core.Item;

public class TravelingSalesman
{
	private int totalItems = 12;
	private int popSize = 100;

	private Item[] items;
	private GeneticAlgorithm geneticAlgo;

	public TravelingSalesman(Item[] items)
	{
		this.items = items;
		this.totalItems = items.length;

		List<Integer> order = new ArrayList<Integer>();

		for(int i = 0; i < totalItems; i++)
		{
			order.add(i);
		}

		geneticAlgo = new GeneticAlgorithm(items, popSize);

		List<Integer> orderCopy = new ArrayList<Integer>(order.size());
		for (Integer i : order)
		{
			orderCopy.add(i);
		}

		for (int i = 0; i < popSize; i++)
		{
			Collections.shuffle(orderCopy);
			geneticAlgo.getPopulation().add(orderCopy);
		}
	}
}
