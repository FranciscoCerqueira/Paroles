package com.onemanarmy.paroles.Game;

import android.os.Bundle;

public interface IGameFactory
{
	IGame getNewGame();
    IGame getNewGame(Bundle savedState);
}
