package com.example.smartshoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import processing.android.CompatUtils;
import processing.android.PFragment;
import processing.core.PApplet;

public class MainActivity extends AppCompatActivity
{
	private PApplet sketch;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		FrameLayout frame = new FrameLayout(this);
		frame.setId(CompatUtils.getUniqueViewId());
		setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		sketch = new ProcessingTest();
		PFragment fragment = new PFragment(sketch);
		fragment.setView(frame, this);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
	{
		if (sketch != null)
		{
			sketch.onRequestPermissionsResult(
					requestCode, permissions, grantResults);
		}
		else
		{
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	public void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		if (sketch != null) {
			sketch.onNewIntent(intent);
		}
	}
}