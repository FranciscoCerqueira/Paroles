package com.onemanarmy.paroles.EndGame;

public class OneWord
{
	private String word;
	private int points;

	public OneWord(String word, int points)
	{
		this.word = word;
		this.points = points;
	}

	public void setWord(String word)
	{
		this.word = word;
	}

	public String getWord()
	{
		return this.word;
	}

	public void setPoints(int points)
	{
		this.points = points;
	}

	public int getPoints()
	{
		return this.points;
	}
}
