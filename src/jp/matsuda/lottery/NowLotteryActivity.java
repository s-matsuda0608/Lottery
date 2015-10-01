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

/**
 * @author excite_2
 * 抽選画面のActivity
 * 入力された抽選項目のリストと抽選数を受け取り、抽選項目のリスト中から抽選数分の要素を選び
 * 当選リストとして抽選結果画面に渡す。
 */
public class NowLotteryActivity extends AppCompatActivity{

	/** 抽選項目のリスト */
	private ArrayList<String> lotteryList = new ArrayList<String>();
	
	/** 当選した項目のリスト */
	private ArrayList<String> winnersList = new ArrayList<String>();
	
	/** 抽選している様子を表示するTextView */
	private TextView lotteryView;
	
	/** 当選した項目のリストを表示するLinearLayout */
	private LinearLayout winnersView;
	
	/** 次へボタン */
	private Button nextButton;
	
	/** 抽選数 */
	private String lotteryQuantity;
	
	/** SoundPoolのインスタンス */
	private SoundPool soundPool;
	
	/** ドラムロールの音楽ファイルのID */
	private int drumrollId;
	
	/** シンバル音の音楽ファイルのID */
	private int cymbalId;
	
	/** ドラムロール再生ID stopするときに使用する*/
	private int drumrollPlayingId;
	
	/** シンバル音再生ID stopするときに使用する*/
	private int cymbalPlayingId;

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
		
		//音楽のファイルのロード
		soundPool = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
		drumrollId = soundPool.load(this, R.raw.drumroll, 1);
		cymbalId = soundPool.load(this, R.raw.cymbal,1);
		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		//音楽がロードされたままになることを防ぐ
		soundPool.unload(drumrollId);
		soundPool.unload(cymbalId);
		System.out.println("call onPause()");
	}

	/**
	 * 次へボタンのリスナを登録する
	 */
	private void setListener() {
		nextButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onClickNextButton();
			}
		});
	}

	/**
	 * シンバル音のストップ
	 * 当選した項目のリストのサイズが抽選数より小さい時は次の抽選を開始。
	 * 当選した項目のリストのサイズが抽選数より大きい時は抽選を終了。
	 */
	private void onClickNextButton() {
		int lotteryQuantityInt = 0;
		try{
			lotteryQuantityInt = Integer.parseInt(lotteryQuantity);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}

		soundPool.stop(cymbalPlayingId);

		if(winnersList.size() < lotteryQuantityInt){
			beginNextLottery();
		}else{
			startNextActivity();
		}
	}

	/**
	 * このActivityをfinishして次のActivityをスタートする。
	 * このとき当選したものリストを次のActivityで受け取れるようにする。
	 */
	private void startNextActivity() {

		Intent intent = new Intent(getApplication(), LotteryResultActivity.class);
		intent.putStringArrayListExtra(Const.WINNERS_LIST, winnersList);

		startActivity(intent);
		NowLotteryActivity.this.finish();
	}

	/**
	 * 抽選中に次へボタンを押せないようにする。
	 * CountDownTimerをセット・スタートし、ドラムロールを鳴らす。
	 */
	private void beginNextLottery() {
		nextButton.setEnabled(false);
		MyCountDownTimer cdt = new MyCountDownTimer();

		drumrollPlayingId = soundPool.play(drumrollId,1.0F,1.0F,0,-1,1.0F);
		cdt.start();

	}

	//端末のバックキーを無効にする
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

	
	/**
	 * @param str
	 * パラメータとして受け取ったStingを、TextViewとして当選リストのビューに追加する。
	 */
	private void addWinningItemView(String str) {
		TextView tv = new TextView(this);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(20);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setText(str);
		winnersView.addView(tv);

	}


	/**
	 * @author excite_2
	 * カウントダウン中、ドラムロールを鳴らす、lotteryViewのTextを入れ替える。
	 * カウントダウン終了後、シンバルの音を鳴らす、lotteryViewにランダムで選出された項目を表示する。
	 */
	class MyCountDownTimer extends CountDownTimer{

		int index;

		/**
		 * CountDownTimerクラスのコンストラクタを呼び出す。
		 */
		public MyCountDownTimer() {
			super(4000, 100);
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
			
			cymbalPlayingId = soundPool.play(cymbalId,1.0F,1.0F,0,-1,1.0F);

			lotteryList.remove(winnerIndex);
			winnersList.add(winner);

			nextButton.setEnabled(true);

		}

	}

}
