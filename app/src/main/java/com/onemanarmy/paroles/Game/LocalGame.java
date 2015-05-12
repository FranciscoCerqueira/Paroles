package com.onemanarmy.paroles.Game;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.*;

public class LocalGame implements IGame
{

	List<String> words = null;
	List<String> descs = null;
	List<String> wordsFound = null;
	List<Integer> pointsByWord = null;
	IPointsCalculator pointsCalculator = null;
	String currentWord = null;
	int counter = 0;
	int points = 0;
    int tips = 0;
	int swipes = 0;
	int showDescs = 0;

	public LocalGame(List<String> words,
					 List<String> descs,
	                 IPointsCalculator pc)
	{
		this.words = words;
		this.descs = descs;
		this.pointsCalculator = pc;
        this.tips = 3;
		this.swipes = 3;
		this.showDescs = 3;
		this.wordsFound = new ArrayList<String>();
		this.pointsByWord = new ArrayList<Integer>();
	}
	
	@Override
	public String nextWord()
	{
		if (counter >= this.words.size())
			counter = 0;
			
		this.currentWord = this.words.get(counter++);
		
		return this.currentWord;
	}

	@Override
	public void addWordPoints(int tips)
	{
		int points = this.pointsCalculator
		                 .GetTotal(this.currentWord, tips);
						   
		
		this.points += points;
		this.wordsFound.add(this.currentWord);
		this.pointsByWord.add(points);
	}

	@Override
	public int getPoints()
	{
		return this.points;
	}
	
	@Override
	public int getPointsByLetter(Character c)
	{
		return this.pointsCalculator
			       .GetValue(c);
	}

    @Override
    public void setPoints(int points)
    {
        this.points = points;
    }

    @Override
    public void setNextWord(int index)
    {
        this.counter = index;
    }

    @Override
    public void saveState(Bundle savedInstance)
    {
        savedInstance.putInt(GameConstants.GAME_INDEX, this.counter - 1);
        savedInstance.putInt(GameConstants.GAME_POINTS, this.points);
		savedInstance.putInt(GameConstants.GAME_TIPS, this.tips);
		savedInstance.putInt(GameConstants.GAME_SWIPES, this.swipes);
		savedInstance.putInt(GameConstants.GAME_DESCS, this.showDescs);
		savedInstance.putStringArray(GameConstants.GAME_WORDSFOUND, this.wordsFound.toArray(new String[this.wordsFound.size()]));
		
		int[] x = new int[this.pointsByWord.size()];
		int i = 0;
		for(Integer n : this.pointsByWord)
			x[i++] = n;
		savedInstance.putIntArray(GameConstants.GAME_POINTSBYWORD, x);
    }

    @Override
    public void setTips(int tips)
    {
        this.tips = tips;
    }

    @Override
    public boolean getTip()
    {
        if (this.tips > 0)
        {
            this.tips--;
            return true;
        }
        else
        {
            return false;
        }
    }
	
	@Override
	public int getTotalTips()
	{
		return this.tips;
	}
	
	@Override
	public void setSwipes(int swipes)
	{
		this.swipes = swipes;
	}

	@Override
	public boolean getSwipe()
	{
		if (this.swipes > 0)
        {
            this.swipes--;
            return true;
        }
        else
        {
            return false;
        }
	}

	@Override
	public int getTotalSwipes()
	{
		return this.swipes;
	}
	
	@Override
	public String getDescription()
	{
		int currentDesc = counter - 1;
		
		if (this.showDescs > 0 &&
			this.descs.size() > currentDesc)
        {
            this.showDescs--;
            return this.descs.get(currentDesc);
        }
        else
        {
            return "";
        }
	}
	
	@Override
	public void setDescriptions(int descs)
	{
		this.showDescs = descs;
	}

	@Override
	public int getTotalDescriptions()
	{
		return this.showDescs;
	}
	
	@Override
	public void setWordsFound(String[] words)
	{
		this.wordsFound = Arrays.asList(words);
	}

	@Override
	public void setPointsByWord(Integer[] points)
	{
		this.pointsByWord = Arrays.asList(points);
	}
	
}
