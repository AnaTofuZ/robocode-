/**
 * Copyright (c) 2001-2014 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 * 所謂コピーライト
 */
package sample;


import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.RateControlRobot;
import robocode.ScannedRobotEvent;

//必要なクラスをimport

/**
 *Rate control robot クラスのサンプル
 * 
 * @author Joshua Galecki (original)
 */
public class VelociRobot extends RateControlRobot { //RateControlRobotの継承

	int turnCounter; //int型変数を宣言
	public void run() { //runメソッド

		turnCounter = 0;//turnCounterを初期化し，右に15回転
		setGunRotationRate(15);
		
		while (true) { //無限ループ
			if (turnCounter % 64 == 0) { //turnCounterを64で割った余りが0ならば
				// 被弾したらターン後まっすぐに直す
				setTurnRate(0);
				//速度4で前進(setメソッドなのですぐには実行されない)
				setVelocityRate(4);
			}
			if (turnCounter % 64 == 32) { //turnCounterを64で割った余りが32なら
				// 素早く後退する
				setVelocityRate(-6);
			}
			turnCounter++; //turnCounterをインクリメント
			execute(); //全ての待機動作を実行
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		fire(1);//敵機を発見したら威力1で砲撃
	}

	public void onHitByBullet(HitByBulletEvent e) {
		//ほかのロボットと混乱させるようにターンを行う 
		setTurnRate(5);//5ピクセルでターンをする様にセット
	}
	
	public void onHitWall(HitWallEvent e) {
		//壁から離れる 
		setVelocityRate(-1 * getVelocityRate());
		//速度をgetVelocityRateに-1をかけたものにして前進
	}
}
