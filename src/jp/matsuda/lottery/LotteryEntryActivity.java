package jp.matsuda.lottery;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class LotteryEntryActivity extends AppCompatActivity {

	TableLayout table;
	Button addButton;
	Button doneButton;
	TextView lotteryQuantityForm;
	int formListSize;
	ArrayList<String> lotteryList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_entry);

		table = (TableLayout) findViewById(R.id.table);
		addButton = (Button) findViewById(R.id.addButton);
		doneButton = (Button) findViewById(R.id.doneButton);
		lotteryQuantityForm = (TextView) findViewById(R.id.lottery_quantity);
		formListSize = 0;

		setListener();

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

	private void addRowView(String str) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.xml.entry_container, null);
		final EditText lotteryForm = (EditText) rowView.findViewById(R.id.lotteryForm);

		formListSize++;
		rowView.setId(formListSize);
		lotteryForm.setText("");

		table.addView(rowView,table.getChildCount());

	}

	public void deleteRowView(View v){
		TableRow tr = (TableRow) v.getParent();
		int rowNumber = tr.getId();

		table.removeView(tr);

		if(formListSize > 1){
			for(int i = rowNumber + 1; i < formListSize; i++){
				TableRow tableRow = (TableRow)findViewById(i);
				tableRow.setId(i - 1);
			}
		}
		formListSize--;
		
		deleteLotteryQuantity();

	}

	private void deleteLotteryQuantity() {
		lotteryQuantityForm.setText("");
	}

	private void setListener() {

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
				if(table.getChildCount() > 1){
					onClickLotteryQuantityForm();
				}else{
					Toast.makeText(getApplicationContext(),Const.MORE_INPUT_MSG, Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	private void onClickAddButton() {
		addRowView("");

	}

	private void onClickDoneButton() {
		if(hasBlankData()){
			Toast.makeText(getApplicationContext(),Const.HAS_BLANK_DATA_MSG, Toast.LENGTH_SHORT).show();
		}else{
			Intent intent = new Intent(getApplication(), LotteryConfirmActivity.class);
			for(int i = 0; i < table.getChildCount(); i++){
				TableRow tr = (TableRow) table.getChildAt(i);
				LinearLayout ll = (LinearLayout)tr.getChildAt(0);
				EditText ed = (EditText)ll.getChildAt(0);
				String str = ed.getText().toString();
				lotteryList.add(str);
			}
			Gson gson = new Gson();
			String lotteryListStr = gson.toJson(lotteryList);
			intent.putExtra(Const.LOTTERY_LIST, lotteryListStr);
			intent.putExtra(Const.LOTTERY_QUANTITY, lotteryQuantityForm.getText().toString());
			int requestCode  = Const.REQUEST_CODE;
			startActivityForResult(intent, requestCode);
		}
	}

	private void onClickLotteryQuantityForm() {

		AlertDialog .Builder lotteryQuantityFormDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.xml.number_picker, null);
		final NumberPicker np = (NumberPicker) v.findViewById(R.id.numberPicker);

		np.setMaxValue(table.getChildCount() - 1);
		np.setMinValue(0);
		np.setValue(0);

		lotteryQuantityFormDialog.setView(np);
		lotteryQuantityFormDialog.setTitle("抽選数入力");

		lotteryQuantityFormDialog.setPositiveButton(Const.OK, new DialogInterface.OnClickListener () {
			public void onClick(DialogInterface dialog, int which) {
				lotteryQuantityForm.setText(String.valueOf(np.getValue()));
			}	
		});		

		lotteryQuantityFormDialog.setNegativeButton(Const.CANCEL, new DialogInterface.OnClickListener(){		
			public void onClick(DialogInterface dialog, int which) {	
				//何もしない
			}	
		});

		lotteryQuantityFormDialog.create().show();		


	}

	private boolean hasBlankData() {
		try{
			for(int i = 0; i <formListSize; i++){
				System.out.println("count : "+i);
				TableRow tr = (TableRow) table.getChildAt(i);
				LinearLayout ll = (LinearLayout)tr.getChildAt(0);
				EditText ed = (EditText)ll.getChildAt(0);
				System.out.println(i+" : "+ed.getText().toString());
				if(ed.getText().equals(null) || ed.getText().toString().equals("")){
					return true;
				}
			}
			if(lotteryQuantityForm.getText().equals(null) || lotteryQuantityForm.getText().toString().equals("")){
				return true;
			}
			
		}catch(IndexOutOfBoundsException e){	
			return true;
		}
		System.out.println("false");
		return false;	

	}

}
