package com.onemanarmy.paroles.Bootstrap;

import com.onemanarmy.paroles.*;
import com.onemanarmy.paroles.Game.*;
import com.onemanarmy.paroles.EndGame.*;

public class DIContainer
{
	public static void BuildUp(MainActivity main)
	{
		IPointsCalculatorFactory pcf = new PointsCalculatorFactory();
		main.setGameFactory(new LocalGameFactory(pcf));
	}
	
	public static void BuildUp(EndGameActivity ega)
	{
		IOneWordFactory owf = new OneWordFactory();
		ega.setOneWordFactory(owf);
	}
}
