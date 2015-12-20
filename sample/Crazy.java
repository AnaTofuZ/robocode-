/**
 * Copyright (c) 2001-2014 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 *所謂コピーライト
 */
package sample;

//Crazy.javaが収納されているpackage名

import robocode.*;

import java.awt.*;

//Crazy.javaで使用するためにimportしてきたクラス

/**
 * Crazy - a sample robot by Mathew Nelson.
 * <p/>
 * このロボットはクレイジーなパターンであちらこちらに動く
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 * crazy の作者についてのcomment
 */

public class Crazy extends AdvancedRobot {
 //AdcancedRobotを継承しCrazyクラスとして実装

	boolean movingForward; //boolean型変数movindForwardを宣言

	/**
	 * runメソッド : Crazyのメインとなるrunメソッド(関数)
	 */

	public void run() {
		// 機体の色の設定 
		setBodyColor(new Color(0, 200, 0));
		setGunColor(new Color(0, 150, 50));
		setRadarColor(new Color(0, 100, 100));
		setBulletColor(new Color(255, 255, 100));
		setScanColor(new Color(255, 200, 200));

		//無限ループ 
		while (true) {
			// ゲーム中40000pixel移動する.(sampleプログラムなので長い数字なら何でも良い)
			setAhead(40000); 
			/**
			  *AdvancedRobotクラスのメソッドsetAheadに4000pixelを渡している
			  *set系のメソッドは宣言してもすぐには実行されずキャッシュに保存される
			  *このメソッドが実際に動くのはexecute()メソッドを呼び出すか，実行動作を行う時である。
			  */

			movingForward = true;

			//変数movingForwardをtrueにする

			//setTurnRightメソッドに90を入れる。(数値は角度として渡される)
			//setTurnRightメソッドは機体を右に回転させるメソッドである
			//これもsetAheadメソッドと同様に実行動作が行われるまで待機される。		
	
			setTurnRight(90);

			waitFor(new TurnCompleteCondition(this));

			/**
			  *waitForメソッドはexecutesメソッドの様に即座に実行されるメソッドであり
			  *このメソッドを読み込んだ瞬間setAhead,setTurnRightが実行される
			  *waitForメソッドは条件設定したTurnCompleteConditionクラスが
			  *完了した事を読み取るまで戻されない
			  *
			  *TurnCompleteConditionは回転が完了したかどうかを判断するクラスなので
			  *setTurnRight(90)が完了するまで保持される。
		       	  */

			setTurnLeft(180);

			//setTurnLeftメソッドに数値180を渡す。
			//動きはsetTurnRightメソッドの逆で左回転する

	
			waitFor(new TurnCompleteCondition(this));

			//先ほどと同様にwaitForメソッドを読み込んだ瞬間に左回転が開始される

			setTurnRight(180);

			//setTurnRightメソッドに180の数値を渡す

			waitFor(new TurnCompleteCondition(this));
			// 同様にwaitForメソッドで確認を取る。ここまでがループされる
		}
	}

	/**
	 * onHitWall: 壁と衝突した際に使用されるメソッド 
	 */
	public void onHitWall(HitWallEvent e) { //イベント処理より壁に当たったことを受け取った場合
		//跳ね返るような動きをする 
		reverseDirection();
			//下に定義されているreverseDirectionメソッドを呼び出す

	}

	/**
	 * reverseDirection: 何かに衝突した際に呼び出され，前進か交代をsetするメソッド
	 */
	public void reverseDirection() {
		if (movingForward) {  //boolean型変数movingForwardがtrueだった場合(runメソッド内でtrueにされている)
			setBack(40000); //40000pixel戻るようにsetを行う(直ちに処理は行われない)
			movingForward = false; //movingForwardをfalseにsetする


		} else { 		//movingForwardがfalseだった場合

			setAhead(40000); //40000pixel直進するようにset
			movingForward = true; //movingForwardをtrueにする
		}
	}

	/**
	 * onScannedRobotメソッド:ロボットをscanした場合，砲撃する
	 */
	public void onScannedRobot(ScannedRobotEvent e) { //ScannnedRobotEventから値を受け取った時
		fire(1);  //威力1で砲撃
	}

	/**
	 * onHitRobot:  戻る
	 */
	public void onHitRobot(HitRobotEvent e) { //敵機に衝突した場合


		if (e.isMyFault()) { //自分から当たった場合
			reverseDirection();  //reverseDirectionを呼び出す
		}
	}
}
