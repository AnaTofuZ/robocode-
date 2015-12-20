/**
 * Copyright (c) 2001-2014 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 * 所謂コピーライト
 */
package sample;


import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;

//TrackFire.javaを構成する上で必要なクラスをimport

/**
 * TrackFire - a sample robot by Mathew Nelson.
 * <p/>
 * じっと座っている。周回し，砲撃するのを一番近いロボットが発見出来た時に行う
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class TrackFire extends Robot {

	/**
	 * TrackFire's runメソッド 
	 */
	public void run() {
		//色の設定 一通りピンク 
		setBodyColor(Color.pink);
		setGunColor(Color.pink);
		setRadarColor(Color.pink);
		setScanColor(Color.pink);
		setBulletColor(Color.pink);

		//永遠ループ 
		while (true) {
			turnGunRight(10); //砲身を右に10度回転 
		}
	}

	/**
	 * onScannedRobot: 敵機を発見したら砲撃 
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		//ロボットの位置を計算 
		double absoluteBearing = getHeading() + e.getBearing();
		//double型変数absoluteBearingを宣言かつ
		//現在の自機の角度と敵機との相対角度を足した値に設定
	
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
		//double型変数bearingFromGunを宣言かつ
		//敵機の絶対角度から自機の主砲絶対角度を引いたものに設定		

		//充分に近ければ砲撃 
		if (Math.abs(bearingFromGun) <= 3) {  //もしbearingFromGunの絶対値が3以下なら
			turnGunRight(bearingFromGun);//その分右回転(絶対値)
			//fireメソッドを使いたいので自機の熱量を確認 
			//また，回転した時に他のロボットがいないか発見を行う 
			
			if (getGunHeat() == 0) { //もし，砲身の熱量が0ならば
				fire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
				//3からbearingFromGunの絶対値を引いた数か，getEnergyから0.1を引いた数
				//そのうちの小さい方の威力で砲撃
			}
		} 
		
		else {  //それ以外は
			turnGunRight(bearingFromGun); ///bearingFromGunの値分右回転
		}
		
		//基本的にscanは自動的に行われるのでこのプログラムは自発的には殆どscanしない
		// bearingFromGunが0の時だけscanする
		if (bearingFromGun == 0) {
			scan();
		}
	}

	public void onWin(WinEvent e) { //勝利時に呼び出されるメソッド
		// 勝利の舞
		turnRight(36000);//右に36000度回転
	}
}				
