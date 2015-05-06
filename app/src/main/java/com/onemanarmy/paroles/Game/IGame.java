package com.onemanarmy.paroles.Game;

import android.os.Bundle;

public interface IGame
{
	String nextWord();
	void addWordPoints(int tips);
	int getPoints();
	int getPointsByLetter(Character c);
    void setPoints(int points);
    void setNextWord(int index);
    void saveState(Bundle savedInstance);
    void setTips(int tips);
    boolean getTip();
	int getTotalTips();
	void setSwipes(int swipes);
	boolean getSwipe();
	int getTotalSwipes();
	void setDescriptions(int descs);
	String getDescription();
	int getTotalDescriptions();
}
