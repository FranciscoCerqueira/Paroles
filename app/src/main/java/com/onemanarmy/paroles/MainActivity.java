package com.onemanarmy.paroles;

import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.onemanarmy.paroles.Bootstrap.*;
import com.onemanarmy.paroles.Game.*;
import java.util.*;
import android.widget.FrameLayout.*;

public class MainActivity extends Activity 
{
	// UI Elements
	private ArrayList<Button> buttons= new ArrayList<Button>();
	private ArrayList<Button> letters= new ArrayList<Button>();
	private View.OnClickListener buttonClick = null;
	private View.OnClickListener letterClick = null;
    private View.OnClickListener tipClick = null;
	private View.OnClickListener swipeClick = null;
	private View.OnClickListener descsClick = null;
	private TextView txtScore = null;
    private TextView txtTime = null;
	private TextView txtDesc = null;
	private Button btnTips = null;
	private Button btnSwipes = null;
	private Button btnDescs = null;
	private DisplayMetrics metrics = null;
	private int sizeForButtons = 0;
	private int sizeForFonts = 0;

    // Supporting game logic
	private String currentWord = "";
	private String shuffledWord = "";
	private String tippedWord = "";
	private IGame game = null;
	private Random rand = null;
	private int tipsInCurrentWord = 0;

    // Timer
    private Handler timer = null;
    private Runnable timerRun = null;
    private boolean started = false;
    private long startTime = 0;
	
	// Swipe
	private float x1;
	private float y1;
    private static final int MIN_DISTANCE = 150;
	
	// For properties use
	private IGameFactory gameFactory = null;

    // Activity Lifecycle
    private Bundle savedInstance = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // It might be used by other methods...
        this.savedInstance = savedInstanceState;
		this.rand = new Random();

        // Fill dependencies
        DIContainer.BuildUp(this);

        // Setup the screen
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		sizeForButtons = (int) (metrics.widthPixels / 10 * 0.95);
		sizeForFonts = (int) (metrics.widthPixels / 25 * 0.9);
		
		PrepareListeners();
		PrepareScreenElements();

        // Finally, start the game
		StartGame();
		
		ShowMessage(Float.toString(metrics.widthPixels / metrics.density) + " x " + 
					Float.toString(metrics.heightPixels / metrics.density));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putLong(GameConstants.GAME_STARTTIME, startTime);
        outState.putString(GameConstants.GAME_SHUFFLEDWORD, shuffledWord);
        outState.putString(GameConstants.GAME_WORDINCONSTRUCTION, GetWordInConstruction());
        outState.putBooleanArray(GameConstants.GAME_BUTTONSENABLED, GetEnabledButtons());
		outState.putString(GameConstants.GAME_TIPPEDWORD, this.tippedWord);
		outState.putString(GameConstants.GAME_CURRENTDESC, this.txtDesc.getText().toString());
		outState.putInt(GameConstants.GAME_TIPSINCURRENTWORD, this.tipsInCurrentWord);
        this.game.saveState(outState);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        started = false;
        timer.removeCallbacks(timerRun);
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{     
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				x1 = event.getX();
				y1 = event.getY();
				break;         
			case MotionEvent.ACTION_UP:
                float x2 = event.getX();
				float y2 = event.getY();

				float deltaX = x2 - x1;
				float deltaY = y2 - y1;

				if (Math.abs(deltaX) > MIN_DISTANCE)
				{
					if (x2 < x1)
					{
						swipeToNextWord();
					}
				}

				if (Math.abs(deltaY) > MIN_DISTANCE)
				{
					if (y1 < y2)
					{
						ResetSolution(false);
						ShowTips();
					}
				}
				                  
				break;   
				
		}
		
		return super.onTouchEvent(event);       
	}

    //------------------------------
	//
	// Properties
	//
	//------------------------------
	
	public void setGameFactory(IGameFactory gameFactory)
	{
		this.gameFactory = gameFactory;
	}
	
	//------------------------------
	//
	// UI plumbing
	//
	//------------------------------
	
	private void PrepareListeners()
    {
        buttonClick = new View.OnClickListener()
        {
            @Override
            public void onClick(View p1)
            {
                if (buttons.contains(p1))
                {
                    String letter = ((Button) p1).getText().toString();
                    letters.get(GetWordInConstruction().length()).setText(letter);
                    p1.setEnabled(false);

                    CheckAnswer();
                }
            }
        };

        letterClick = new View.OnClickListener()
        {
            @Override
            public void onClick(View p1)
            {
                ResetSolution(false);
				ShowTips();
            }
        };

        timerRun = new Runnable()
        {
            @Override
            public void run()
            {
                if (started)
                    ShowTime();

                timer.postDelayed(timerRun, 1000L);
            }
        };

        tipClick = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (game.getTip())
                {
					
					int pos = -1;
					
					while(pos == -1)
					{
						pos = rand.nextInt(currentWord.length());
						if (!tippedWord.substring(pos, pos + 1).equalsIgnoreCase("?"))
							pos = -1;
						else
						{
							String tempWord = "";
							for(int i=0; i < currentWord.length(); i++)
							{
								
								if (i == pos)
									tempWord += "x";
								else
									tempWord += tippedWord.substring(i, i+1);
							}
							tippedWord = tempWord;
						}
					}
					
					tipsInCurrentWord++;
					ResetSolution(false);
					ShowTips();
					CheckAnswer();
                }
            }
        };
		
		swipeClick = new View.OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				swipeToNextWord();
			}
		};
		
		descsClick = new View.OnClickListener()
        {
            @Override
            public void onClick(View p1)
            {
				if (txtDesc.getText().length() == 0)
				{
                	ShowDescription(game.getDescription());
					ShowTotalDescriptions();
				}
            }
        };
    }
	
	private void PrepareScreenElements()
	{
		SetButton(R.id.board_button1);
		SetButton(R.id.board_button2);
		SetButton(R.id.board_button3);
		SetButton(R.id.board_button4);
		SetButton(R.id.board_button5);
		SetButton(R.id.board_button6);
		SetButton(R.id.board_button7);
		SetButton(R.id.board_button8);
		SetButton(R.id.board_button9);
		SetButton(R.id.board_button10);
		
		SetLetter(R.id.board_solution_letter1);
		SetLetter(R.id.board_solution_letter2);
		SetLetter(R.id.board_solution_letter3);
		SetLetter(R.id.board_solution_letter4);
		SetLetter(R.id.board_solution_letter5);
		SetLetter(R.id.board_solution_letter6);
		SetLetter(R.id.board_solution_letter7);
		SetLetter(R.id.board_solution_letter8);
		SetLetter(R.id.board_solution_letter9);
		SetLetter(R.id.board_solution_letter10);
		
		txtScore = (TextView) this.findViewById(R.id.board_score);
		txtScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeForFonts);
		
        txtTime = (TextView) this.findViewById(R.id.board_time);
		txtTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeForFonts);
		
		txtDesc = (TextView) this.findViewById(R.id.board_description);
		txtDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeForFonts);
		
		btnTips = (Button) this.findViewById(R.id.board_tip);
		btnTips.setOnClickListener(tipClick);
		RedimButton(btnTips, false);
		
		btnSwipes = (Button) this.findViewById(R.id.board_swipe);
		btnSwipes.setOnClickListener(swipeClick);
		RedimButton(btnSwipes, false);
		
		btnDescs = (Button) this.findViewById(R.id.board_descs);
		btnDescs.setOnClickListener(descsClick);
		RedimButton(btnDescs,false);
		
	}
	
	private void SetButton(int id)
	{
		Button b = (Button) this.findViewById(id);
		b.setOnClickListener(buttonClick);
		
		RedimButton(b, true);
		
		buttons.add(b);
	}
	
	private void SetLetter(int id)
	{
		Button b = (Button) this.findViewById(id);
		b.setOnClickListener(letterClick);
		
		RedimButton(b,true);
		
		letters.add(b);
	}

    private boolean[] GetEnabledButtons()
    {
        boolean[] state = new boolean[10];
        int counter = 0;

        for(Button b : buttons)
            state[counter++] = b.isEnabled();

        return state;
    }
	
	private void RedimButton(Button b, boolean changeWidth)
	{
		android.view.ViewGroup.LayoutParams p = b.getLayoutParams();
		
		if (changeWidth)
			p.width = sizeForButtons;
			
		p.height = sizeForButtons;
		b.setLayoutParams(p);
		
		b.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeForFonts);
	}
	
	//------------------------------
	//
	//  Adjusting the game....
	//
	//------------------------------
	
	private void StartGame()
	{
        // Check if the activity was recreated...
        if (savedInstance == null)
            this.game = this.gameFactory.getNewGame();
        else
        {
            this.game = this.gameFactory.getNewGame(savedInstance);
            startTime = savedInstance.getLong(GameConstants.GAME_STARTTIME);
        }

        // Put the current game elements on screen...
        ShowPoints();
        SetUIForNextWord(true);

        // Now the user will be able to start, so I'll start the timer
        // here when its the first passage to this block
        if (startTime == 0)
            startTime = System.currentTimeMillis();

        // Putting the timer to work...
        timer = new Handler();
        timer.postDelayed(timerRun, 1000L);

        ShowTime();
        started = true;

	}
	
	private void SetUIForNextWord(boolean checkSavedInstance)
	{
		this.currentWord = this.game.nextWord();

        if (checkSavedInstance &&
            savedInstance != null)
        {
            this.shuffledWord = savedInstance.getString(GameConstants.GAME_SHUFFLEDWORD);
			this.tippedWord = savedInstance.getString(GameConstants.GAME_TIPPEDWORD);
			ShowDescription(savedInstance.getString(GameConstants.GAME_CURRENTDESC));
			this.tipsInCurrentWord = savedInstance.getInt(GameConstants.GAME_TIPSINCURRENTWORD);
        }
        else
        {
			PrepareForTips();
            ScrambleWord();
			ShowDescription("");
			this.tipsInCurrentWord = 0;
        }

		SetUIWithWord();
		ResetSolution(checkSavedInstance);
		ShowTips();
		ShowSwipes();
		ShowTotalDescriptions();
	}
	
	private void SetUIWithWord()
	{
		for(int i=0; i<buttons.size(); i++)
		{
			Button b = buttons.get(i);
			Button l = letters.get(i);
			if (i < this.shuffledWord.length())
			{
				b.setVisibility(View.VISIBLE);
				l.setVisibility(View.VISIBLE);
				b.setText(this.shuffledWord.substring(i, i + 1));
				l.setText("?");
			}
			else
			{
				b.setVisibility(View.GONE);
				l.setVisibility(View.GONE);
				b.setText("");
				l.setText("");
			}
		}
	}
	
	private void ResetSolution(boolean checkSavedInstance)
	{
        if (checkSavedInstance &&
            this.savedInstance != null)
        {
            String wordInConstruction = savedInstance.getString(GameConstants.GAME_WORDINCONSTRUCTION);
            boolean[] buttonsEnabled = savedInstance.getBooleanArray(GameConstants.GAME_BUTTONSENABLED);

            for (int i = 0; i < this.currentWord.length(); i++)
            {
                buttons.get(i).setEnabled(buttonsEnabled[i]);
                if (i < wordInConstruction.length())
                    letters.get(i).setText(wordInConstruction.substring(i, i + 1));
            }
        }
        else
        {
            for (int i = 0; i < this.currentWord.length(); i++)
            {
                buttons.get(i).setEnabled(true);
                letters.get(i).setText("?");
            }
        }

	}
	
	private void CheckAnswer()
	{
		if (this.GetWordInConstruction().length() == 
		    this.currentWord.length())
		{
			
			if (this.GetWordInConstruction().equals(this.currentWord))
			{
				CalculatePoints();
				SetUIForNextWord(false);
			}
			else
			{
				ResetSolution(false);
				ShowTips();
			}
		}
	}
	
	public void ScrambleWord()
	{
		List<Character> l = new ArrayList<>();
		for(char c :  currentWord.toCharArray()) 
			l.add(c); 
			
		Collections.shuffle(l); 

		StringBuilder sb = new StringBuilder(); //now rebuild the word
		for(char c : l)
			sb.append(c);

		shuffledWord = sb.toString();
		
		// Avoid situations where the word appears
		// on ui exactly as it is
		if (shuffledWord.equals(currentWord))
			ScrambleWord();
	}
	
	public void PrepareForTips()
	{
		this.tippedWord = "";
		for(char c : currentWord.toCharArray()) 
			this.tippedWord = this.tippedWord.concat("?");
	}
	
	public void CalculatePoints()
	{
		this.game.addWordPoints(this.tipsInCurrentWord);
		ShowPoints();
	}

    public void ShowPoints()
    {
        txtScore.setText(Integer.toString(this.game.getPoints()));
    }

    public void ShowTime()
    {
        long seconds = 120 - ((System.currentTimeMillis() - startTime) / 1000);
        txtTime.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
    }
	
	public void ShowTips()
	{
		int tips = this.game.getTotalTips();
		
		btnTips.setText("? x" + tips);
		btnTips.setEnabled(tips > 0);
		
		for(int i=0;i < this.currentWord.length(); i++)
		{
			if(this.tippedWord.substring(i,i+1).equals("x"))
			{
				String letter = this.currentWord.substring(i,i+1);
				letters.get(i).setText(letter);
				
				for(Button b : buttons)
				{
					if (b.getText().toString().equalsIgnoreCase(letter) &&
						b.isEnabled())
					{
						b.setEnabled(false);
						break;
					}
				}
			}
		}
	}
	
	public void ShowSwipes()
	{
		int swipes = this.game.getTotalSwipes();

		btnSwipes.setText("-> x" + swipes);
		btnSwipes.setEnabled(swipes > 0);
	}
	
	public String GetWordInConstruction()
	{
		String word = "";
		
		for(Button b : letters)
		{
			String letter = b.getText().toString();
			if (b.getVisibility() == View.VISIBLE && !letter.equalsIgnoreCase("?"))
				word += letter;
			else
				break;
		}
			
		return word;
	}
	
	private void swipeToNextWord()
	{
		if (this.game.getSwipe())
			SetUIForNextWord(false);
		else
			ShowMessage(this.getString(R.string.no_swipes));
	}
	
	private void ShowDescription(String desc)
	{
		txtDesc.setText(desc);
	}
	
	public void ShowTotalDescriptions()
	{
		int descs = this.game.getTotalDescriptions();

		btnDescs.setText("! x" + descs);
		btnDescs.setEnabled(descs > 0);
	}

	//------------------------------
	//
	//  Helping to debug
	//
	//------------------------------
	
	public void ShowMessage(String msg)
	{
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
	}
}
