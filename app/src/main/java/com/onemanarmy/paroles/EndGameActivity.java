package com.onemanarmy.paroles;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import com.onemanarmy.paroles.EndGame.*;
import com.onemanarmy.paroles.Bootstrap.*;
import java.util.*;

public class EndGameActivity extends Activity
{
	public static final String END_GAME_WORDS = "END_GAME_WORDS";
	public static final String END_GAME_POINTS = "END_GAME_POINTS";
	
	private String[] wordsFound = null;
	private int[] pointsByWord = null;
	
	private IOneWordFactory oneWordFactory = null;
	private ArrayList<OneWord> items = null;
	
	private ListView lv = null;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_endgame);
		
		DIContainer.BuildUp(this);
		
		this.wordsFound = getIntent().getStringArrayExtra(EndGameActivity.END_GAME_WORDS);
		this.pointsByWord = getIntent().getIntArrayExtra(EndGameActivity.END_GAME_POINTS);
		
		this.items = new ArrayList<OneWord>();
		int i = 0;
		for(String w : this.wordsFound)
		{
			items.add(this.oneWordFactory.create(w, this.pointsByWord[i]));
			i++;
		}
		
		this.lv = (ListView) this.findViewById(R.id.activity_endgame_list);
		this.lv.setAdapter(new EndGameAdapter(this.items));
	}
	
	public void setOneWordFactory(IOneWordFactory owf)
	{
		this.oneWordFactory = owf;
	}
	
	private class EndGameAdapter extends ArrayAdapter<OneWord>
	{ 
		public EndGameAdapter(ArrayList<OneWord> words) 
		{ 
			super(EndGameActivity.this, R.layout.oneword,R.id.oneword_word,words);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View row = super.getView(position, convertView, parent);
			OneWord w = getItem(position);
			
			if (row != null)
			{
				TextView wordTextView = (TextView) row.findViewById(R.id.oneword_word);
				if (wordTextView != null)
					wordTextView.setText(w.getWord());

				TextView pointsTextView = (TextView) row.findViewById(R.id.oneword_points);
				if (pointsTextView != null)
					pointsTextView.setText(Integer.toString(w.getPoints())); 
			}
			
			return row;
		}
	}
	
}
