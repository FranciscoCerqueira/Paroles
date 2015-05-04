package com.onemanarmy.paroles.Game;
import android.os.Bundle;

import java.util.*;
import android.widget.TextView.*;

public class LocalGameFactory implements IGameFactory
{
	private IPointsCalculatorFactory pcFactory = null;
	
	public LocalGameFactory(IPointsCalculatorFactory pcFactory)
	{
		this.pcFactory = pcFactory;
	}

	@Override
	public IGame getNewGame()
	{
		ArrayList<String> colors = new ArrayList<String>();
		colors.add("WHITE");
		colors.add("BLACK");
		colors.add("BROWN");
		colors.add("PURPLE");
		colors.add("BLUE");
		colors.add("GREEN");
		colors.add("YELLOW");
		colors.add("ORANGE");
		colors.add("MAGENTA");
		
		ArrayList<String> descs = new ArrayList<String>();
		descs.add("A light color");
		descs.add("A dark color");
		descs.add("A color");
		descs.add("Another color");
		descs.add("A basic color");
		descs.add("A color");
		descs.add("It is a color");
		descs.add("One Color");
		descs.add("A color again");
		
		LocalGame game = new LocalGame(colors, 
									   descs,
		                               this.pcFactory.create());
		return game;
	}

    public IGame getNewGame(Bundle savedState)
    {
        // In this case, I don't care about saving the
        // list of words, but the definitive class probably
        // wants it...
        IGame result = getNewGame();
        if (savedState != null)
        {
            result.setNextWord(savedState.getInt(GameConstants.GAME_INDEX));
            result.setPoints(savedState.getInt(GameConstants.GAME_POINTS));
			result.setTips(savedState.getInt(GameConstants.GAME_TIPS));
			result.setSwipes(savedState.getInt(GameConstants.GAME_SWIPES));
			result.setDescriptions(savedState.getInt(GameConstants.GAME_DESCS));
        }

        return result;
    }

}
