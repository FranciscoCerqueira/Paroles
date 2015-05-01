package com.onemanarmy.paroles.Bootstrap;

import com.onemanarmy.paroles.*;
import com.onemanarmy.paroles.Game.*;

public class DIContainer
{
	public static void BuildUp(MainActivity main)
	{
		IPointsCalculatorFactory pcf = new PointsCalculatorFactory();
		main.setGameFactory(new LocalGameFactory(pcf));
	}
}
