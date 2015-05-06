package com.onemanarmy.paroles.Game;

public class PointsCalculatorFactory implements IPointsCalculatorFactory
{
	@Override
	public IPointsCalculator create()
	{
		return new OneLetterOnePointCalculator();
	}
	
}
