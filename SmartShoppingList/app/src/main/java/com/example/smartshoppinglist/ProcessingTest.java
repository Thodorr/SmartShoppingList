package com.example.smartshoppinglist;

import processing.core.PApplet;

public class ProcessingTest extends PApplet
{
	public void settings()
	{
		size(600, 600);
	}

	public void setup()
	{

	}

	public void draw()
	{
		background(0);

		if(mousePressed)
			ellipse(mouseX, mouseY, 50, 50);
	}
}
