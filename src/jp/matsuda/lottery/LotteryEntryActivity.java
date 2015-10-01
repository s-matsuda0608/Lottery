package jp.matsuda.lottery;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author excite_2
 * 抽選エントリー画面のActivity
 * 入力フォームを表示し、任意数ある抽選項目と抽選数の入力を受け付ける。
 */
public class LotteryEntryActivity extends AppCompatActivity {

	/** 抽選項目の入力欄を追加するTableLayout */
	TableLayout table;

	/** 入力欄追加ボタン */
	Button addButton;

	/** 入力完了ボタン */
	Button doneButton;

	/** 抽選数のTextView */
	TextView lotteryQuantityForm;

	/** 抽選項目入力欄の数 */
	int formListSize;

	/** 内部データとして保持する抽選項目のリスト */
	ArrayList<String> lotteryList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_entry);

		//ビューの取得
		table = (TableLayout) findViewById(R.id.table);
		addButton = (Button) findViewById(R.id.addButton);
		doneButton = (Button) findViewById(R.id.doneButton);
		lotteryQuantityForm = (TextView) findViewById(R.id.lottery_quantity);
		formListSize = 0;

		setListener();

	}

	/**
	 * @param str
	 * tableにentry_container.xmlで定義されたTableRowを追加する。
	 * 追加するTableRowが持つlotteryFormにパラメータのstrをセットする。
	 * TableRowを追加した時にformListSizeをインクリメントする。
	 */
	private void addRowView(String str) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.xml.entry_container, null);
		final EditText lotteryForm = (EditText) rowView.findViewById(R.id.lotteryForm);

		formListSize++;
		rowView.setId(formListSize);
		lotteryForm.setText("");

		table.addView(rowView,table.getChildCount());

	}

	/**
	 * @param v
	 * tableからパラメータvの親であるTableRowを削除する。
	 * formListSizeをデクリメントする。
	 * lotteryQuantityFormのtextを消去する。
	 */
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

	/**
	 * lotteryQuantityFormにブランクのStringをセットする。
	 */
	private void deleteLotteryQuantity() {
		lotteryQuantityForm.setText("");
	}

	/**
	 * 各ボタン、および、lotteryQuantityFormのリスナ登録をする。
	 */
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
					onClickLotteryQuantityForm();
			}
		});
	}

	/**
	 * 入力欄を持つ行を追加する。
	 */
	private void onClickAddButton() {
		addRowView("");

	}

	/**
	 * 入力に不備がないかチェックし、エラーがあれば、エラーメッセージを出力。
	 * エラーが無ければ、フォームのデータを取得し、次の画面で受け取れるようにする。
	 */
	private void onClickDoneButton() {

		if(hasBlankData()){
			Toast.makeText(getApplicationContext(),Const.HAS_BLANK_DATA_MSG, Toast.LENGTH_SHORT).show();

		}else{

			Intent intent = new Intent(getApplication(), LotteryConfirmActivity.class);
			lotteryList.clear();

			//抽選項目の取得
			for(int i = 0; i < table.getChildCount(); i++){
				TableRow tr = (TableRow) table.getChildAt(i);
				LinearLayout ll = (LinearLayout)tr.getChildAt(0);
				EditText ed = (EditText)ll.getChildAt(0);
				String str = ed.getText().toString();
				lotteryList.add(str);
			}

			//データを渡す
			intent.putStringArrayListExtra(Const.LOTTERY_LIST, lotteryList);
			intent.putExtra(Const.LOTTERY_QUANTITY, lotteryQuantityForm.getText().toString());

			startActivity(intent);
		}
	}

	/**
	 * 抽選項目入力欄の数を取得し、2より小さかった場合はエラーメッセージ出力。
	 * そうでない場合は、NumberPickerを持ったダイアログを表示し、抽選数の入力を受け付ける。
	 * 入力された抽選数をlotteryQuantityFormに表示する。
	 */
	private void onClickLotteryQuantityForm() {

		if(table.getChildCount() < 2){
			Toast.makeText(getApplicationContext(),Const.MORE_INPUT_MSG, Toast.LENGTH_SHORT).show();

		}else{
			AlertDialog .Builder lotteryQuantityFormDialog = new AlertDialog.Builder(this);
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.xml.number_picker, null);
			final NumberPicker np = (NumberPicker) v.findViewById(R.id.numberPicker);

			np.setMaxValue(table.getChildCount() - 1);
			np.setMinValue(1);
			np.setValue(1);

			lotteryQuantityFormDialog.setView(np);
			lotteryQuantityFormDialog.setTitle("抽選数入力");

			lotteryQuantityFormDialog.setPositiveButton(Const.OK, new DialogInterface.OnClickListener () {
				public void onClick(DialogInterface dialog, int which) {
					lotteryQuantityForm.setText(String.valueOf(np.getValue()));
				}	
			});

			lotteryQuantityFormDialog.setNegativeButton(Const.CANCEL, null);

			lotteryQuantityFormDialog.create().show();
		}
	}

	/**
	 * @return 抽選項目入力フォームや抽選数入力フォームが空であるかどうか
	 */
	private boolean hasBlankData() {
		
		try{
			for(int i = 0; i <formListSize; i++){
				TableRow tr = (TableRow) table.getChildAt(i);
				LinearLayout ll = (LinearLayout)tr.getChildAt(0);
				EditText ed = (EditText)ll.getChildAt(0);
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
		return false;	

	}

}
