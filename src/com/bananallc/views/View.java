package com.bananallc.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;

public class View extends Group{

	public View()
	{
		super();
	}
	
	public void setDimensions(float x, float y, float w, float h, float maxW, float maxH)
	{
		float width = 0;
		float height = 0;
		
		float wRatio = getRatio(w, maxW);
		float hRatio = getRatio(h, maxH);
		
		float wParent = Gdx.graphics.getWidth();
		float hParent = Gdx.graphics.getHeight();
		if (getParent() != null)
		{
			wParent = getParent().getWidth();
			hParent = getParent().getHeight();
		}
		
		//If two ratios available, select smaller one
		if (wRatio > 0 || hRatio > 0)
		{
			if ((hRatio <= 0) || hRatio < wRatio)
			{
				width = convertPercent(wParent, maxW);
				height = convertPercent(hParent, calcRatioHeight(maxW, w, h));
			}
			else if ((wRatio <= 0) || wRatio < hRatio)
			{
				width = convertPercent(wParent, calcRatioWidth(maxH, w, h));
				height = convertPercent(hParent, maxH);
			}
			else
			{
				width = convertPercent(wParent, maxW);
				height = convertPercent(hParent, maxH);
			}
		}
		
		setWidth(width);
		setHeight(height);
	}
	
	private float getRatio(float regular, float max)
	{
		float ratio = 0;
		if (regular > 0 && max > 0)
			ratio = max / regular;
		return ratio;
	}
	
	private float calcRatioWidth(float calcH, float aspectW, float aspectH)
	{
		return (calcH * aspectW) / aspectH;
	}
	
	private float calcRatioHeight(float calcW, float aspectW, float aspectH)
	{
		return (calcW * aspectH) / aspectW;
	}
	
	private float convertPercent(float dimension, float percent)
	{
		return dimension * percent / 100;
	}
}
