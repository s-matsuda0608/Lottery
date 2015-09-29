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

public class LotteryConfirmActivity extends AppCompatActivity{

	private LinearLayout lotteryItemView;
	private TextView lotteryQuantityView;
	private Button OKButton;
	private Button backButton;
	private ArrayList<String> lotteryList = new ArrayList<String>();
	private String lotteryQuantity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_confirm);

		OKButton = (Button) findViewById(R.id.OKButton);
		backButton = (Button) findViewById(R.id.backButton);
		lotteryItemView = (LinearLayout) findViewById(R.id.lotteryItemView); 
		lotteryQuantityView = (TextView) findViewById(R.id.lotteryQuantityView);

		Intent intent = getIntent();

		lotteryList = intent.getStringArrayListExtra(Const.LOTTERY_LIST);
		lotteryQuantity = intent.getStringExtra(Const.LOTTERY_QUANTITY);

		createConfirmView();
		setListener();

	}


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
	
	private void onClickOKButton() {
		Intent intent = new Intent(getApplication(), NowLotteryActivity.class);

		intent.putExtra(Const.LOTTERY_LIST, lotteryList);
		intent.putExtra(Const.LOTTERY_QUANTITY, lotteryQuantity);

		startActivity(intent);
		LotteryConfirmActivity.this.finish();
	}

	private void onClickbackButton() {
		finish();
	}

	private void createConfirmView() {

		lotteryItemView.removeAllViews();
		
		for(int i = 0; i < lotteryList.size(); i++){	
			TextView tv = new TextView(this);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(20);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setText(lotteryList.get(i));
			lotteryItemView.addView(tv);
		}	
		lotteryQuantityView.setText(lotteryQuantity);	


	}
}
