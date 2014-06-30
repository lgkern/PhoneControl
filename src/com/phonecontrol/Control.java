package com.phonecontrol;

public class Control 
{
	public Control(int index, String name, int[] elements, int move)
	{
		this.Index = index;
		this.Name = name;
		this.Elements = new int[elements.length];
		for(int i = 0; i<elements.length; i++)
		{
			this.Elements[i] = elements[i];
		}
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

}
