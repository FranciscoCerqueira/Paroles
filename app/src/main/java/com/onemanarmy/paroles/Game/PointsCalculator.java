package com.onemanarmy.paroles.Game;
import java.util.*;

public class PointsCalculator implements IPointsCalculator
{
	private HashMap<Character, Integer> letterValues;
	
	public PointsCalculator()
	{
		this.letterValues = new HashMap<Character, Integer>();
		this.letterValues.put('A', 2); //
		this.letterValues.put('B', 5); //
		this.letterValues.put('C', 3); //
		this.letterValues.put('D', 3); //
		this.letterValues.put('E', 1); //
		this.letterValues.put('F', 5); //
		this.letterValues.put('G', 4); //
		this.letterValues.put('H', 4); //
		this.letterValues.put('I', 2); //
		this.letterValues.put('J', 10); //
		this.letterValues.put('K', 6); //
		this.letterValues.put('L', 3); //
		this.letterValues.put('M', 4); //
		this.letterValues.put('N', 2); //
		this.letterValues.put('O', 2); //
		this.letterValues.put('P', 4); //
		this.letterValues.put('Q', 10); //
		this.letterValues.put('R', 2); //
		this.letterValues.put('S', 2); //
		this.letterValues.put('T', 2); //
		this.letterValues.put('U', 4); //
		this.letterValues.put('V', 6); //
		this.letterValues.put('W', 6); //
		this.letterValues.put('X', 9); //
		this.letterValues.put('Y', 5); //
		this.letterValues.put('Z', 8); //
		
	}

	@Override
	public int GetValue(Character c)
	{
		return this.letterValues.get(c);
	}

	@Override
	public int GetTotal(String word)
	{
		int total = 0;
		for(Character c : word.toCharArray())
			total+=this.letterValues.get(c);
			
		return total;
	}
}
