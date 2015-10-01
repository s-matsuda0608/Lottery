package jp.matsuda.lottery;

/**
 * @author excite_2
 * パッケージjp.matsuda.lotteryで用いるリテラルの定義のみしているConstanceクラス
 * privateなコンストラクタを定義することによりインスタンスの生成をさせない。
 */
public class Const {
	
	//インスタンスの生成をさせないようにする
	protected Const(){}

	
	/** 抽選項目のリストをActivity間でやり取りするときのキー */
	protected static final String LOTTERY_LIST = "lotteryList";
	
	/** 抽選数をActivity間でやり取りするときのキー */
	protected static final String LOTTERY_QUANTITY = "lotteryQuantity";
	
	/** 当選した項目のリストをActivity間でやり取りするときのキー */
	protected static final String WINNERS_LIST = "winnersListStr";
	
	/** 入力チェックをした結果、入力されていない項目があった時に表示するメッセージ */
	protected static final CharSequence HAS_BLANK_DATA_MSG = "未入力欄があります";
	
	/** 抽選対象の項目が少ない時に表示するメッセージ */
	protected static final CharSequence MORE_INPUT_MSG = "抽選対象を増やしてください";
	
	/** ダイアログのOKボタン */
	protected static final String OK = "OK";
	
	/** ダイアログのキャンセルボタン */
	protected static final String CANCEL = "キャンセル";
	

}
