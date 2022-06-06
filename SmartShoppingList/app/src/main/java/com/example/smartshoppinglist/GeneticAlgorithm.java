package com.example.smartshoppinglist;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm
{
	private Item[] items;

	private List<List<Integer>> population;
	private int populationSize;
	private float[] fitness;

	private float recordDistance;
	private List<Integer> bestEver;
	private List<Integer> currentBest;

	public GeneticAlgorithm(Item[] items, int populationSize)
	{
		this.items = items;
		this.populationSize = populationSize;

		population = new ArrayList<>(populationSize);
		fitness = new float[populationSize];
		recordDistance = Float.POSITIVE_INFINITY;
	}

	private void nextGeneration()
	{
		List<List<Integer>> newPopulation = new ArrayList<>(populationSize);
		for(int i = 0; i < population.size(); i++)
		{
			List<Integer> orderA = pickOne();
			List<Integer> orderB = pickOne();
			List<Integer> order = crossover(orderA, orderB);
			mutate(order, 0.01f);
			newPopulation.add(order);
		}
		population = newPopulation;
	}

	private void calculateFitness()
	{
		float currentRecord = Float.POSITIVE_INFINITY;
		for(int i = 0; i < population.size(); i++)
		{
			float dst = calculateDistance(items, population.get(i));
			if(dst < recordDistance)
			{
				recordDistance = dst;
				bestEver = population.get(i);
			}
			if(dst < currentRecord)
			{
				currentRecord = dst;
				currentBest = population.get(i);
			}

			fitness[i] = (float) (1 / (Math.pow(dst, 8) + 1));
		}
	}

	private void normalizeFitness()
	{
		float sum = 0f;
		for(int i = 0; i < fitness.length; i++)
		{
			sum += fitness[i];
		}
		for(int i = 0; i < fitness.length; i++)
		{
			fitness[i] /= sum;
		}
	}

	private List<Integer> pickOne()
	{
		int index = 0;
		float r = (float) Math.random();

		while(r > 0)
		{
			r -= fitness[index];
			index ++;
		}
		index = index <= 0 ? 0 : index - 1;

		return copyList(population.get(index));
	}

	private List<Integer> crossover(List<Integer> orderA, List<Integer> orderB)
	{
		int max = orderA.size();
		int min = 0;
		int range = max - min + 1;
		int start = (int) Math.floor((Math.random() * range) + 1);

		min = start + 1;
		range = max - min + 1;
		int end = (int) Math.floor((Math.random() * range) + 1);

		List<Integer> newOrder = new ArrayList<Integer>();
		for(int i = start; i < end; i++)
		{
			newOrder.add(orderA.get(i));
		}

		for(int i = 0; i < orderB.size(); i++)
		{
			int item = orderB.get(i);
			if(!newOrder.contains(item))
			{
				newOrder.add(item);
			}
		}

		return newOrder;
	}

	private void mutate(List<Integer> order, float mutateProb)
	{
		for(int i = 0; i < items.length; i++)
		{
			if(Math.random() < mutateProb)
			{
				int max = order.size();
				int min = 0;
				int range = max - min + 1;
				int indexA = (int) Math.floor((Math.random() * range) + 1);
				int indexB = (indexA + 1) % items.length;

				swap(order, indexA, indexB);
			}
		}
	}

	private float calculateDistance(Item[] items, List<Integer> order)
	{
		float sum = 0f;
		for(int i = 0; i < order.size() - 1; i++)
		{
			int itemAIndex = order.get(i);
			Item item_A = items[itemAIndex];
			float posX_A = item_A.getPosX();
			float posY_A = item_A.getPosY();

			int itemBIndex = order.get(i + 1);
			Item item_B = items[itemBIndex];
			float posX_B = item_B.getPosX();
			float posY_B = item_B.getPosY();

			float dst = dist(posX_A, posY_A, posX_B, posY_B);
			sum += dst;
		}

		return sum;
	}

	private float dist(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	private void swap(List<Integer> a, int i, int j)
	{
		int temp = a.get(i);
		a.set(i, a.get(j));
		a.set(j, temp);
	}

	private List<Integer> copyList(List<Integer> list)
	{
		List<Integer> copiedList = new ArrayList<Integer>(list.size());
		for(Integer i : list)
		{
			copiedList.add(i);
		}

		return copiedList;
	}
}
