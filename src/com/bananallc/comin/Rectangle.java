package com.bananallc.comin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Rectangle extends Actor{

	private ShapeRenderer sr;
	private Color[] colors;
	
	public Rectangle(final float x, final float y, final float w, final float h, final Color[] colors)
	{
		sr = new ShapeRenderer();
		
		this.colors = colors;
		
		setWidth(w);
		setHeight(h);
		setPosition(x, y);
	}
	
	@Override
	public void draw(SpriteBatch batch, float arg1){
		batch.end();
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.setTransformMatrix(batch.getTransformMatrix());
		
		sr.begin(ShapeType.FilledRectangle);
		if (colors.length == 1)
		{
			sr.setColor(colors[0]);
			sr.filledRect(getX(), getY(), getWidth(), getHeight());
		}
		else if (colors.length == 4)
		{
			sr.filledRect(getX(), getY(), getWidth(), getHeight(), colors[0], colors[1], colors[2], colors[3]);
		}
		sr.end();
		batch.begin();
	}
}
