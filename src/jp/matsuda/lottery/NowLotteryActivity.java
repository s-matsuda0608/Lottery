package jp.matsuda.lottery;

import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
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

	private ArrayList<String> lotteryList = new ArrayList<String>();
	private ArrayList<String> winnersList = new ArrayList<String>();
	private TextView lotteryView;
	private LinearLayout winnersView;
	private Button nextButton;
	private String lotteryQuantity;
	
	private SoundPool soundPool;
	private int drumrollId;
	private int winnersSoundId;
	private int drumrollPlayingId;
	private int winnersPlayingId;

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

	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume(){
		super.onResume();
		
		soundPool = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
		drumrollId = soundPool.load(this, R.raw.drumroll, 1);
		winnersSoundId = soundPool.load(this, R.raw.cymbal,1);
		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		soundPool.unload(drumrollId);
		soundPool.unload(winnersSoundId);
		System.out.println("call onPause()");
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

		soundPool.stop(winnersPlayingId);	

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
		nextButton.setEnabled(false);
		MyCountDownTimer cdt = new MyCountDownTimer(4000, 100);

		drumrollPlayingId = soundPool.play(drumrollId,1.0F,1.0F,0,-1,1.0F);
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
			lotteryView.setText(lotteryList.get(index));	

			if(index == lotteryList.size() - 1){	
				index = 0;
			}else{	
				index++;
			}

		}

		@Override
		public void onFinish() {
			
			Random rnd = new Random();
			int winnerIndex = rnd.nextInt(lotteryList.size());
			String winner = lotteryList.get(winnerIndex);

			System.out.println("drumroll停止処理始まり");
			soundPool.stop(drumrollPlayingId);
			System.out.println("drumroll停止処理終わり");
			
			lotteryView.setText(winner);
			addWinningItemView(winner);
			
			winnersPlayingId = soundPool.play(winnersSoundId,1.0F,1.0F,0,-1,1.0F);

			lotteryList.remove(winnerIndex);
			winnersList.add(winner);

			nextButton.setEnabled(true);

		}

	}

}
