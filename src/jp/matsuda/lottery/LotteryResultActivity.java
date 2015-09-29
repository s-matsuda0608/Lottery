package jp.matsuda.lottery;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LotteryResultActivity extends AppCompatActivity{

	Button appRestartButton;
	LinearLayout winnersView;
	ArrayList<String> winnersList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_result);

		appRestartButton = (Button) findViewById(R.id.appRestartButton);
		winnersView = (LinearLayout) findViewById(R.id.winnersView); 

		Intent intent = getIntent();
		
		winnersList = intent.getStringArrayListExtra(Const.WINNERS_LIST);

		createWinersView();
		setListener();

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction()==KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				return true;
			}
		}
		return super.dispatchKeyEvent(event);		
	}				


	private void setListener() {
		appRestartButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onClickAppRestartButton();
			}
		});
	}

	private void onClickAppRestartButton() {
		Intent intent = new Intent(getApplication(), LotteryEntryActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void createWinersView() {
		for(int i = 0; i < winnersList.size(); i++){	
			TextView tv = new TextView(this);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(20);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setText(winnersList.get(i));
			winnersView.addView(tv);
		}	
	}

}
