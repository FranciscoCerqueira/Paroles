package com.onemanarmy.paroles.EndGame;

public class OneWordFactory implements IOneWordFactory
{
	@Override
	public OneWord create(String word, int points)
	{
		return new OneWord(word,points);
	}
}
