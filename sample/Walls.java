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

import java.awt.*;

//必要なクラスをimport

/**
 * Walls - a sample robot by Mathew Nelson, and maintained by Flemming N. Larsen
 * <p/>
 *外壁にそって動き，顔面に砲撃
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class Walls extends Robot { //Robotの継承

	boolean peek; // もしロボットがいたらそこでターンをしない 
	double moveAmount; // どれくらい移動するかを決定

	/**
	 * runメソッド
	 */
	public void run() {
		//色の設定 
		setBodyColor(Color.black);
		setGunColor(Color.black);
		setRadarColor(Color.orange);
		setBulletColor(Color.cyan);
		setScanColor(Color.cyan);

		//moveAmountをバトルフィールド上での最大値に変更 
		moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
		//mathクラスのmaxメソッドを用いて大きい方の値を代入

		//peekをfalseに設定 
		peek = false;

		//壁に向かって左折 
		// getHeading()の値を90で割った余りの角度分左回転
		turnLeft(getHeading() % 90);
		ahead(moveAmount); //moveAmountの分直進
		// 主砲を90度右回転する
		peek = true;	//peekをtrueに設定
		turnGunRight(90); //主砲をまず右に90度回転
		turnRight(90);	//その後機体を右に90度回転

		while (true) {//永遠ループ
			//この辺りの処理はaheadが完了するまでには終了している 
			peek = true;
			// 壁に向かう
			ahead(moveAmount);
		
			peek = false;
			// peekをfalseに設定
			turnRight(90);
			//機体を右に90度回転
		}
	}

	/**
	 * onHitRobot:  衝突したら少し逃げる.
	 */
	public void onHitRobot(HitRobotEvent e) {
		//正面にいたら後退をする 
		if (e.getBearing() > -90 && e.getBearing() < 90) { //敵機が絶対値90度以下なら
			back(100);//100ピクセル後進
		} //それ以外は逆に進む 
		else {
			ahead(100);//100ピクセル前進
		}
	}

	/**
	 * onScannedRobot:  砲撃!
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		fire(2);//威力2で砲撃

		if (peek) { //peekの値がtrueならscanを行う
			scan();
		}
	}
}
