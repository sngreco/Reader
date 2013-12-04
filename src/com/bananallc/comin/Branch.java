package com.bananallc.comin;

import java.util.List;

public class Branch
{
	public int id;
	public List<Scene> scenes;
	
	public Scene getScene(int id)
	{
		Scene scene = null;
		for (int a = 0; a < scenes.size(); a++)
		{
			if (scenes.get(a).getId() == id)
				scene = scenes.get(a);
		}
		return scene;
	}
}