package ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.GeneticAlgorithm;
import core.Item;
import math.Vector2;
import processing.core.PApplet;
import processing.core.PVector;

public class ProcessingTest extends PApplet
{
	private final int totalItems = 12;
	private final int popSize = 500;

	private Item[] items;
	private GeneticAlgorithm geneticAlgo;
	public void settings()
	{
		size(800, 800);
	}

	public void setup()
	{
		List<Integer> order = new ArrayList<Integer>();

		items = new Item[totalItems];
		for (int i = 0; i < totalItems; i++)
		{
			PVector v = new PVector(random(width), random(height / 2));
			items[i] = new Item("", new Vector2(v.x, v.y));
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

	public void draw()
	{
		background(0);

		geneticAlgo.runOnce();

		stroke(255);
		strokeWeight(4);
		noFill();
		beginShape();
		List<Integer> bestEver = geneticAlgo.getBestEver();
		for (int i = 0; i < bestEver.size(); i++)
		{
			int n = bestEver.get(i);
			PVector position = new PVector(items[n].getPosition().x, items[n].getPosition().y);

			vertex(position.x, position.y);
			ellipse(position.x, position.y, 16, 16);
		}
		endShape();

		translate(0, height / 2);
		stroke(255);
		strokeWeight(4);
		noFill();
		beginShape();
		List<Integer> currentBest = geneticAlgo.getCurrentBest();
		for (int i = 0; i < currentBest.size(); i++)
		{
			int n = currentBest.get(i);
			PVector position = new PVector(items[n].getPosition().x, items[n].getPosition().y);

			vertex(position.x, position.y);
			ellipse(position.x, position.y, 16, 16);
		}
		endShape();
	}
}
