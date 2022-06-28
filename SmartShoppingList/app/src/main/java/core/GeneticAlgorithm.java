package core;

import math.MathUtility;

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

		bestEver = new ArrayList<Integer>();
		currentBest = new ArrayList<Integer>();
		population = new ArrayList<>(populationSize);
		fitness = new float[populationSize];
		recordDistance = Float.POSITIVE_INFINITY;
	}

	public void runOnce()
	{
		calculateFitness();
		normalizeFitness();
		nextGeneration();
	}

	private void nextGeneration()
	{
		List<List<Integer>> newPopulation = new ArrayList<>(populationSize);
		for (int i = 0; i < populationSize; i++)
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
		for (int i = 0; i < population.size(); i++)
		{
			float dst = calculateDistance(items, population.get(i));
			if (dst < recordDistance)
			{
				recordDistance = dst;
				bestEver = population.get(i);
			}
			if (dst < currentRecord)
			{
				currentRecord = dst;
				currentBest = population.get(i);
			}

			fitness[i] = (float) (1 / (Math.pow(dst, 8) + 1));
		}

		normalizeFitness();
	}

	private void normalizeFitness()
	{
		float sum = 0f;
		for (int i = 0; i < fitness.length; i++)
		{
			sum += fitness[i];
		}
		for (int i = 0; i < fitness.length; i++)
		{
			fitness[i] /= sum;
		}
	}

	private List<Integer> pickOne()
	{
		int index = 0;
		float r = (float) Math.random();

		while (r > 0 && index < fitness.length)
		{
			r -= fitness[index];
			index++;
		}
		index = index <= 0 ? 0 : index - 1;

		return MathUtility.copyList(population.get(index));
	}

	private List<Integer> crossover(List<Integer> orderA, List<Integer> orderB)
	{
		int start = (int) MathUtility.randomRange(0, orderA.size() - 1);
		int end = (int) MathUtility.randomRange(start + 1, orderA.size());

		List<Integer> newOrder = new ArrayList<Integer>();
		for (int i = start; i < end; i++)
		{
			newOrder.add(orderA.get(i));
		}

		for (int i = 0; i < orderB.size(); i++)
		{
			int item = orderB.get(i);
			if (!newOrder.contains(item))
			{
				newOrder.add(item);
			}
		}

		return newOrder;
	}

	private void mutate(List<Integer> order, float mutateProb)
	{
		for (int i = 0; i < items.length; i++)
		{
			if (Math.random() < mutateProb)
			{
				int indexA = (int) MathUtility.randomRange(0, order.size());
				int indexB = (indexA + 1) % items.length;

				MathUtility.swap(order, indexA, indexB);
			}
		}
	}

	private float calculateDistance(Item[] items, List<Integer> order)
	{
		float sum = 0f;
		for (int i = 0; i < order.size() - 1; i++)
		{
			int itemAIndex = order.get(i);
			Item item_A = items[itemAIndex];

			int itemBIndex = order.get(i + 1);
			Item item_B = items[itemBIndex];

			float dst = MathUtility.dist(item_A.getPosition(), item_B.getPosition());
			sum += dst;
		}

		return sum;
	}

	public List<List<Integer>> getPopulation()
	{
		return population;
	}

	public void setPopulation(List<List<Integer>> population)
	{
		this.population = population;
	}

	public List<Integer> getBestEver()
	{
		return bestEver;
	}

	public void setBestEver(List<Integer> bestEver)
	{
		this.bestEver = bestEver;
	}

	public List<Integer> getCurrentBest()
	{
		return currentBest;
	}

	public void setCurrentBest(List<Integer> currentBest)
	{
		this.currentBest = currentBest;
	}
}
