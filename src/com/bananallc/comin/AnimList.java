package com.bananallc.comin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;


public class AnimList 
{
	private int id;
	private List<Anim> anims;
	public float delay;
	public int repeat;

	public final static int REPEAT_NONE = 0;
	public final static int DELAY_NONE = 0;
	public final static int INVISIBLE = 0;
	public final static int VISIBLE = 100;
	
	//private final static String TAG = "AnimList";
	
	public int getId()
	{
		return id;
	}
	
	public Action getAnims(int width, int height)
	{
		Action actions = new ParallelAction();
		for (int a = 0; a < anims.size(); a++)
		{
			Action action = anims.get(a).getAnim(width, height);
			if (action != null)
				((ParallelAction) actions).addAction(action);
		}
		

		if (delay > DELAY_NONE)
			actions = delay(delay, actions);
		if (repeat < REPEAT_NONE)
			actions = forever(actions);
		else
			actions = repeat(repeat+1, actions);
		
		return actions;
	}
	
	public float calcDuration()
	{
		float duration = 0;
		for (int a = 0; a < anims.size(); a++)
		{
			Anim anim = anims.get(a);
			float temp = anim.delay + anim.duration;
			if (anim.repeat > REPEAT_NONE)
				temp = temp * anim.repeat;
			if (temp > duration)
				duration = temp;
		}
		return duration;
	}
	
	public static class Anim
	{
		public final static int ALPHA = 1;
		public final static int COLOR = 2;
		public final static int MOVE_BY = 3;
		public final static int MOVE_TO = 4;
		public final static int ROTATE_BY = 5;
		public final static int ROTATE_TO = 6;
		public final static int SCALE_BY = 7;
		public final static int SCALE_TO = 8;
		
		public final static int INT_LINEAR = 0;
		public final static int INT_BOUNCE = 1;		
		public final static int INT_BOUNCE_IN = 2;		
		public final static int INT_BOUNCE_OUT = 3;		
		public final static int INT_CIRCLE = 4;		
		public final static int INT_CIRCLE_IN = 5;		
		public final static int INT_CIRCLE_OUT = 6;		
		public final static int INT_ELASTIC = 7;		
		public final static int INT_ELASTIC_IN = 8;		
		public final static int INT_ELASTIC_OUT = 9;		
		public final static int INT_EXP_10 = 10;		
		public final static int INT_EXP_10_IN = 11;		
		public final static int INT_EXP_10_OUT = 12;		
		public final static int INT_EXP_5 = 13;		
		public final static int INT_EXP_5_IN = 14;	
		public final static int INT_EXP_5_OUT = 15;	
		public final static int INT_FADE = 16;	
		public final static int INT_POW_2 = 17;	
		public final static int INT_POW_2_IN = 18;	
		public final static int INT_POW_2_OUT = 19;	
		public final static int INT_POW_3 = 20;	
		public final static int INT_POW_3_IN = 21;	
		public final static int INT_POW_3_OUT = 22;	
		public final static int INT_POW_4 = 23;	
		public final static int INT_POW_4_IN = 24;	
		public final static int INT_POW_4_OUT = 25;	
		public final static int INT_POW_5 = 26;	
		public final static int INT_POW_5_IN = 27;	
		public final static int INT_POW_5_OUT = 28;	
		public final static int INT_SINE = 29;	
		public final static int INT_SINE_IN = 30;	
		public final static int INT_SINE_OUT = 31;	
		public final static int INT_SWING = 32;	
		public final static int INT_SWING_IN = 33;	
		public final static int INT_SWING_OUT = 34;	
		
		public final static int X = 0;
		public final static int Y = 1;
		
		private final static Action INVISIBLE = new Action(){
			@Override
			public boolean act(float arg0) {
				this.actor.setVisible(false);
				return false;
			}};
			
		private final static Action VISIBLE = new Action(){
			@Override
			public boolean act(float arg0) {
				this.actor.setVisible(true);
				return false;
			}};
		
		private final static String TAG = "Anim";
		
		public int type;
		public float[] values;
		public float duration;
		public float delay;
		public int repeat, interpolation;
		
		public Anim(int type, float[] values, float duration, float delay, int repeat, int interpolation)
		{
			this.type = type;
			this.values = values;
			this.duration = duration;
			this.delay = delay;
			this.repeat = repeat;
			this.interpolation = interpolation;
		}
		
		public Action getAnim(int width, int height)
		{
			Action action = null;
			
			if (type == ALPHA)
				action = alpha(values[0]/100, duration, getInterpolation());
			else if (type == COLOR)
				action = color(createColor((int)values[0]), duration, getInterpolation());
			else if (type == MOVE_BY)
				action = moveBy(width * values[X] / 100, height * values[Y] / 100, duration, getInterpolation());
			else if (type == MOVE_TO)
				action = moveTo(width * values[X] / 100, height * values[Y] / 100, duration, getInterpolation());
			else if (type == ROTATE_BY)
				action = rotateBy(-values[0], duration, getInterpolation());
			else if (type == ROTATE_TO)
				action = rotateTo(-values[0], duration, getInterpolation());
			else if (type == SCALE_BY)
				action = scaleBy(values[X]/100, values[Y]/100, duration, getInterpolation());
			else if (type == SCALE_TO)
				action = scaleTo(values[X]/100, values[Y]/100, duration, getInterpolation());

			if (action != null)
			{
				if (delay > DELAY_NONE)
					action = delay(delay, action);
				if (repeat < REPEAT_NONE)
					action = forever(action);
				else
					action = repeat(repeat+1, action);
			}
			
			String log = "type:" + log() + ", values:";
			for (int a = 0; a < values.length; a++)
				log += values[a] + ", ";
			log += "duration:" + duration + ", delay:" + delay + ", repeat:" + repeat;
			Gdx.app.log(TAG, log);
			
			if (action != null && type == ALPHA)
			{
				ParallelAction parallel = new ParallelAction();
				parallel.addAction(action);
				if (values[0] == 0)
					parallel.addAction(delay(duration, INVISIBLE));
				else
					parallel.addAction(VISIBLE);
				return parallel;
			}
			else
				return action;
		}
		
		private Interpolation getInterpolation()
		{
			if (interpolation == INT_BOUNCE)
				return Interpolation.bounce;		
			else if (interpolation == INT_BOUNCE_IN)	
				return Interpolation.bounceIn;
			else if (interpolation == INT_BOUNCE_OUT)
				return Interpolation.bounceOut;
			else if (interpolation == INT_CIRCLE)	
				return Interpolation.circle;
			else if (interpolation == INT_CIRCLE_IN)
				return Interpolation.circleIn;
			else if (interpolation == INT_CIRCLE_OUT)
				return Interpolation.circleOut;
			else if (interpolation == INT_ELASTIC)		
				return Interpolation.elastic;
			else if (interpolation == INT_ELASTIC_IN)
				return Interpolation.elasticIn;
			else if (interpolation == INT_ELASTIC_OUT)	
				return Interpolation.elasticOut;
			else if (interpolation == INT_EXP_10)		
				return Interpolation.exp10;
			else if (interpolation == INT_EXP_10_IN)	
				return Interpolation.exp10In;
			else if (interpolation == INT_EXP_10_OUT)	
				return Interpolation.exp10Out;
			else if (interpolation == INT_EXP_5)		
				return Interpolation.exp5;
			else if (interpolation == INT_EXP_5_IN)	
				return Interpolation.exp5In;
			else if (interpolation == INT_EXP_5_OUT)
				return Interpolation.exp5Out;
			else if (interpolation == INT_FADE)	
				return Interpolation.fade;
			else if (interpolation == INT_POW_2)	
				return Interpolation.pow2;
			else if (interpolation == INT_POW_2_IN)	
				return Interpolation.pow2In;
			else if (interpolation == INT_POW_2_OUT)	
				return Interpolation.pow2Out;
			else if (interpolation == INT_POW_3)	
				return Interpolation.pow3;
			else if (interpolation == INT_POW_3_IN)	
				return Interpolation.pow3In;
			else if (interpolation == INT_POW_3_OUT)	
				return Interpolation.pow3Out;
			else if (interpolation == INT_POW_4)	
				return Interpolation.pow4;
			else if (interpolation == INT_POW_4_IN)	
				return Interpolation.pow4In;
			else if (interpolation == INT_POW_4_OUT)	
				return Interpolation.pow4Out;
			else if (interpolation == INT_POW_5)	
				return Interpolation.pow5;
			else if (interpolation == INT_POW_5_IN)	
				return Interpolation.pow5In;
			else if (interpolation == INT_POW_5_OUT)	
				return Interpolation.pow5Out;
			else if (interpolation == INT_SINE)	
				return Interpolation.sine;
			else if (interpolation == INT_SINE_IN)
				return Interpolation.sineIn;
			else if (interpolation == INT_SINE_OUT)	
				return Interpolation.sineOut;
			else if (interpolation == INT_SWING)	
				return Interpolation.swing;
			else if (interpolation == INT_SWING_IN)	
				return Interpolation.swingIn;
			else if (interpolation == INT_SWING_OUT)	
				return Interpolation.swingOut;
			else 
				return Interpolation.linear;
		}
		
		private String log()
		{
			String log = "";
			if (type == ALPHA)
				log += "alpha";
			else if (type == COLOR)
				log += "color";
			else if (type == MOVE_BY)
				log += "move by";
			else if (type == MOVE_TO)
				log += "move to";
			else if (type == ROTATE_BY)
				log += "rotate by";
			else if (type == ROTATE_TO)
				log += "rotate to";
			else if (type == SCALE_BY)
				log += "scale by";
			else if (type == SCALE_TO)
				log += "scale to";
			return log;
		}
		
		private Color createColor(int value)
		{
			java.awt.Color c = new java.awt.Color(value);
			float r = c.getRed();
			float g = c.getGreen();
			float b = c.getBlue();
			float a = c.getAlpha();
			return new Color(r, g, b, a);			
		}
	}
}
