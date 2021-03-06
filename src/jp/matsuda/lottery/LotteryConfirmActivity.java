package jp.matsuda.lottery;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author excite_2
 * 抽選入力確認画面のActivity
 * 入力情報を画面に表示する。
 */
public class LotteryConfirmActivity extends AppCompatActivity{

	/** 入力された抽選対象項目を表示するLinearLayout */
	private LinearLayout lotteryItemView;
	
	/** 入力された抽選数を表示するTextView */
	private TextView lotteryQuantityView;
	
	/** OKボタン */
	private Button OKButton;
	
	/** 戻るボタン */
	private Button backButton;
	
	/** 入力された抽選項目のリスト */
	private ArrayList<String> lotteryList = new ArrayList<String>();
	
	/** 入力された抽選数 */
	private String lotteryQuantity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_confirm);

		//ビュー取得
		OKButton = (Button) findViewById(R.id.OKButton);
		backButton = (Button) findViewById(R.id.backButton);
		lotteryItemView = (LinearLayout) findViewById(R.id.lotteryItemView); 
		lotteryQuantityView = (TextView) findViewById(R.id.lotteryQuantityView);

		Intent intent = getIntent();

		//入力データ取得
		lotteryList = intent.getStringArrayListExtra(Const.LOTTERY_LIST);
		lotteryQuantity = intent.getStringExtra(Const.LOTTERY_QUANTITY);

		createConfirmView();
		setListener();

	}


	/**
	 * OKボタン、戻るボタンのリスナ登録
	 */
	private void setListener() {
		
		OKButton.setOnClickListener(new OnClickListener(){		
			@Override	
			public void onClick(View v) {	
				onClickOKButton();
			}
		});		

		backButton.setOnClickListener(new OnClickListener(){		
			@Override	
			public void onClick(View v) {	
				onClickbackButton();
			}
		});		
		
	}
	
	/**
	 * OKボタンが押されたときの処理
	 */
	private void onClickOKButton() {
		Intent intent = new Intent(getApplication(), NowLotteryActivity.class);

		intent.putExtra(Const.LOTTERY_LIST, lotteryList);
		intent.putExtra(Const.LOTTERY_QUANTITY, lotteryQuantity);

		startActivity(intent);
		LotteryConfirmActivity.this.finish();
	}

	/**
	 * 戻るボタンが押されたときの処理
	 */
	private void onClickbackButton() {
		finish();
	}

	/**
	 * 入力データを確認するビューの作成
	 */
	private void createConfirmView() {

		lotteryItemView.removeAllViews();
		
		//抽選項目のリストをすべてビューに追加
		for(int i = 0; i < lotteryList.size(); i++){	
			
			TextView tv = new TextView(this);
			
			//TextViewのスタイル設定
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(20);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setText(lotteryList.get(i));
			
			lotteryItemView.addView(tv);
		}	
		
		//抽選数をビューに追加
		lotteryQuantityView.setText(lotteryQuantity);	


	}
}
