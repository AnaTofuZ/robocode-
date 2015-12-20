/**
 * Copyright (c) 2001-2014 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 * 所謂コピーライト
 */
package sample;


import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;

//Trackerを構成する上で必要なクラスをimport

/**
 * Tracker - a sample robot by Mathew Nelson.
 * <p/>
 * 上に行くロボットを見つけたら，近づいて，充分に近づければ砲撃
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class Tracker extends Robot {
	int count = 0; //自機の周回をcountすつための変数を宣言 
	
	double gunTurnAmt; //サーチ時にいくらか主砲を回転させる 
	String trackName; // 現在追跡している敵機の名前を保存

	/**
	 * run:  Tracker's のメインとなるrunメソッド(関数) 
	 */
	public void run() {
		// 色の設定
		setBodyColor(new Color(128, 128, 50));
		setGunColor(new Color(50, 50, 20));
		setRadarColor(new Color(200, 200, 70));
		setScanColor(Color.white);
		setBulletColor(Color.blue);

		// 砲身の準備
		trackName = null; // 現在はまだ何も追跡していないのでnullを入れる
		setAdjustGunForRobotTurn(true); //回転中は主砲を停止
		gunTurnAmt = 10; // 今現在はgunTurnを10に設定

		// 無限ループ
		while (true) {
			// 敵機を探しながら主砲を回転
			turnGunRight(gunTurnAmt);//gunTurnAmt分
			//どれくらい見たかを保存する為にcountをインクリメント 
			count++;
			//二周しても敵機が見つからなかったら左回転 
			if (count > 2) {
				gunTurnAmt = -10;//-10で左回転を意味する
			}
			//5countでも敵機を発見出来なかった場合，右回転に戻す 
			if (count > 5) {
				gunTurnAmt = 10;
			}
			//もし10countしても敵機を発見出来なかった場合，別の機体を標的にする
			if (count > 11) {
				trackName = null;
			}
		}
	}

	/**
	 * onScannedRobot:  敵機を見つけた時に呼び出されるメソッド
	 */
	public void onScannedRobot(ScannedRobotEvent e) {

		//もし，現在ターゲットが決まっていても他の敵機を見つけたら 
		// 更にスキャンイベントを入手することができる
		if (trackName != null && !e.getName().equals(trackName)) {
			//trackNameがnullでなく，getNameがtrackNameの値と違う時
			return; //終了
		}

		//もしターゲットがなくてもやってしまう 
		if (trackName == null) { //trackNameがnullだったら
			trackName = e.getName(); //trackNameに発見した敵機の名前を挿れて
			out.println("Tracking " + trackName); //敵機の名前を出力
		}
		//ターゲットならメインメソッドを参照し，countを0にする 
		count = 0;
		//もしターゲットが遠距離なら，ターゲットに対してターンして移動 
		if (e.getDistance() > 150) { //ターゲットとの距離が150ピクセル以上なら
			gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		//gunTurnAmtに(ターゲットとの相対角度)+(現在の角度-レーダーの角度)の角度を渡す

			turnGunRight(gunTurnAmt); //gunTurnAmtの分だけ砲身右回転 
			turnRight(e.getBearing()); //turnRightに敵機との相対角度を渡す
			ahead(e.getDistance() - 140); //敵機との距離から140ピクセル引いた分前進
			return;
		}

		//もし敵機と近づいていた場合 
		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		fire(3);//威力3で砲撃を追加

		//とても近づいていた場合，バック 
		if (e.getDistance() < 100) { //距離が100以下なら
			if (e.getBearing() > -90 && e.getBearing() <= 90) {//さらに絶対値90以下なら
				back(40);//40ピクセルバック
			} else {
				ahead(40);//それ以外なら40ピクセル前進
			}
		}
		scan();//scan
	}

	/**
	 * onHitRobot:被弾時には当ててきた方を新しいターゲットにする
	 */
	public void onHitRobot(HitRobotEvent e) {
		//このメソッドが呼び出された際にまだターゲットが決まっていない場合 
		if (trackName != null && !trackName.equals(e.getName())) {
			out.println("Tracking " + e.getName() + " due to collision");
		//コメントを出力

		}
		// ターゲットをセット
		trackName = e.getName();

		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		fire(3);
		back(50); //さきほどまでの処理に50ピクセルバックを加える
	}

	/**
	 * onWin:  勝利の舞
	 */
	public void onWin(WinEvent e) {
		for (int i = 0; i < 50; i++) { //iが50になるまで
			turnRight(30);//右30度回転
			turnLeft(30);//左30度回転
		}
	}
}
