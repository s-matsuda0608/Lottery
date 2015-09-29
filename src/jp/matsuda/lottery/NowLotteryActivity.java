package jp.matsuda.lottery;

import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NowLotteryActivity extends AppCompatActivity{

	ArrayList<String> lotteryList = new ArrayList<String>();
	ArrayList<String> winnersList = new ArrayList<String>();
	TextView lotteryView;
	LinearLayout winnersView;
	Button nextButton;
	String lotteryQuantity;
	MediaPlayer drumroll;
	MediaPlayer winnerSound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_now_lottery);

		lotteryView = (TextView) findViewById(R.id.lotteryView);
		winnersView = (LinearLayout) findViewById(R.id.winnersView);
		nextButton = (Button) findViewById(R.id.nextButton);

		Intent intent = getIntent();

		lotteryList = intent.getStringArrayListExtra(Const.LOTTERY_LIST);
		lotteryQuantity = intent.getStringExtra(Const.LOTTERY_QUANTITY);

		setListener();
		initSound();


	}

	private void initSound() {
		drumroll = MediaPlayer.create(this, R.raw.drumroll);
		drumroll .setLooping(true);
		drumroll.seekTo(0);

		winnerSound = MediaPlayer.create(this, R.raw.winners);
		winnerSound.setLooping(true);
		winnerSound.seekTo(0);


	}

	private void setListener() {
		nextButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onClickNextButton();
			}
		});
	}

	private void onClickNextButton() {
		int lotteryQuantityInt = 0;
		try{
			lotteryQuantityInt = Integer.parseInt(lotteryQuantity);
		}catch(NumberFormatException e){	
			e.printStackTrace();
		}	

		winnerSound.stop();	

		if(winnersList.size() < lotteryQuantityInt){	
			beginLottery();
		}else{	
			endAllLottery();
		}	
	}

	private void endAllLottery() {

		Intent intent = new Intent(getApplication(), LotteryResultActivity.class);
		intent.putStringArrayListExtra(Const.WINNERS_LIST, winnersList);

		startActivity(intent);
		NowLotteryActivity.this.finish();
	}

	private void beginLottery() {
		drumroll.start();
		nextButton.setEnabled(true);
		MyCountDownTimer cdt = new MyCountDownTimer(5000, 200);
		cdt.start();


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

	private void addWinningItemView(String str) {
		TextView tv = new TextView(this);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(20);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setText(str);
		winnersView.addView(tv);

	}


	class MyCountDownTimer extends CountDownTimer{

		int index;

		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			index = 0;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			((TextView)findViewById(R.id.lotteryView)).setText(lotteryList.get(index));	

			if(index == lotteryList.size() - 1){	
				index = 0;
			}else{	
				index++;
			}

		}

		@Override
		public void onFinish() {
			drumroll.stop();

			Random rnd = new Random();
			int winnerIndex = rnd.nextInt(lotteryList.size());
			String winner = lotteryList.get(winnerIndex);

			((TextView)findViewById(R.id.lotteryView)).setText(winner);
			addWinningItemView(winner);

			lotteryList.remove(winnerIndex);
			winnersList.add(winner);

			nextButton.setEnabled(true);

			winnerSound.start();


		}

	}

}
