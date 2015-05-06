package com.onemanarmy.paroles.Game;

public class OneLetterOnePointCalculator implements IPointsCalculator
{

	@Override
	public int GetValue(Character c)
	{
		return 1;
	}

	@Override
	public int GetTotal(String word,int tips)
	{
		return word.length() - tips;
	}
}
