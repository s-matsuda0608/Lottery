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

/**
 * @author excite_2
 * 抽選結果画面のActivity
 * 当選した項目を表示し、ボタンを押すと抽選エントリー画面に戻る。
 */
public class LotteryResultActivity extends AppCompatActivity{

	
	/** もう一度ボタン */
	Button appRestartButton;
	
	/** 当選した項目のリストを表示するLinearLayout */
	LinearLayout winnersView;
	
	/** 当選した項目のリスト */
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


	/**
	 * もう一度ボタンのリスナ登録
	 */
	private void setListener() {
		appRestartButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onClickAppRestartButton();
			}
		});
	}

	/**
	 * 抽選エントリー画面に戻る
	 * 始めの抽選エントリー画面のActivityからこのActivityまでのスタックを削除する
	 */
	private void onClickAppRestartButton() {
		Intent intent = new Intent(getApplication(), LotteryEntryActivity.class);
		
		//これまでのActivityスタックの削除するフラグを立てる
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		startActivity(intent);
		
	}

	/**
	 * 当選した項目のリストをwinnersViewに表示する
	 */
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
