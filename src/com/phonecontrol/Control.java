package com.phonecontrol;

public class Control 
{
	public Control(int index, String name, int[] elements, int move)
	{
		this.Index = index;
		this.Name = name;
		if(elements != null)
		{
			this.Elements = new int[elements.length];
			for(int i = 0; i<elements.length; i++)
			{
				this.Elements[i] = elements[i];
			}
		}
		else
			this.Elements = null;
		this.Move = move;
	}
	
	private int Index;
	private String Name;
	private int[] Elements;
	private int Move;
	
	
	public int index()
	{
		return Index;
	}
	
	public String name()
	{
		return Name;
	}
	
	public int[] elements()
	{
		return Elements;
	}
	
	public int Move()
	{
		return Move;
	}
	
	public void updateElements(int[] elementMap)
	{
		
		int ac = 0;
		for(int i = 0; i < 9; i++)
		{
			ac+=elementMap[i];			
		}
		
		int[] newElements = new int[ac];
		int count = 0;
		for(int i = 0; i < 9; i++)
		{
			if(elementMap[i] == 1)
			{
				newElements[count] = i;
				count++;
			}
		}
		
		this.Elements = newElements;
	}
	
	public String getDBFormat()
	{
		String result = "";
		result+=this.Name;
		result+=";";
		for(int element : this.Elements)
		{
			result+=element+"-";
		}
		result+=";";
		result+=this.Move+"\n";
		return result;
	}

}
