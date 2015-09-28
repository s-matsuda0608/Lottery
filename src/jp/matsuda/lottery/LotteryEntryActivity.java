package jp.matsuda.lottery;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.os.Bundle;

public class LotteryEntryActivity extends AppCompatActivity {

	ViewGroup table;
	Button addButton;
	Button doneButton;
	TextView lotteryQuantityForm;
	AlertDialog.Builder lotteryQuantityFormDialog;
	int formListSize;
	ArrayList<String> lotteryList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_entry);

		table = (ViewGroup) findViewById(R.id.table);
		addButton = (Button) findViewById(R.id.addButton);
		doneButton = (Button) findViewById(R.id.doneButton);
		lotteryQuantityForm = (TextView) findViewById(R.id.lottery_quantity);

		SetListener();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if(resultCode == RESULT_OK && requestCode == R.layout.activity_lottery_confirm && null != intent) {		
			Gson gson = new Gson();	
			lotteryList = gson.fromJson(intent.getStringExtra(Const.LOTTERY_LIST),ArrayList.class);	
			String lotteryQuantity = intent.getStringExtra(Const.LOTTERY_QUANTITY);	

			lotteryQuantityForm.setText(lotteryQuantity);	
			for(int i = 0; i < lotteryList.size(); i++){	
				addRowView(lotteryList.get(i));
			}	
		}	
	}

	private void addRowView(String string) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	private void SetListener() {

		addButton.setOnClickListener(new OnClickListener(){	
			@Override
			public void onClick(View v) {
				onClickAddButton();
			}
		});

		doneButton.setOnClickListener(new OnClickListener(){	
			@Override
			public void onClick(View v) {
				onClickDoneButton();
			}
		});

		lotteryQuantityForm.setOnClickListener(new OnClickListener(){	
			@Override
			public void onClick(View v) {
				onClickLotteryQuantityForm();
			}
		});

	}

	private void onClickAddButton() {
		// TODO 自動生成されたメソッド・スタブ

	}

	private void onClickDoneButton() {
		// TODO 自動生成されたメソッド・スタブ

	}

	private void onClickLotteryQuantityForm() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
