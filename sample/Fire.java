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
import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;

//Fire.javaで使用するためのクラスをimport

/**
 * Fire - a sample robot by Mathew Nelson, and maintained.
 * <p/>
 * Fireは銃身を回転させながら，砲撃して移動する.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 * 作者の説明とちょっとした概要
 */


public class Fire extends Robot {
	int dist = 50; //被弾した際に動く距離として使用するためにint型変数dirstを宣言し，50を代入 

	/**
	 * run:  Fireのメインとなるrunメソッド
	 */
	public void run() {
		//色の設定 
		setBodyColor(Color.orange);
		setGunColor(Color.orange);
		setRadarColor(Color.red);
		setScanColor(Color.red);
		setBulletColor(Color.red);

		//ゆっくりと砲身を右回転し続ける 
		
		while (true) {
			turnGunRight(5); //5度右回転
		}
	}

	/**
	 * onScannedRobot:  敵機を検知したら砲撃
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		//敵機が50pixel以下の距離，かつ自機のエネルギーが50以上の場合 
		// 最高威力で砲撃!

		if (e.getDistance() < 50 && getEnergy() > 50) { 
				
			//e.getDistance()でscanした敵機との距離を図り
			//getEnergyで自機のエネルギーを数値化する

			fire(3);	//最大出力で砲撃

		} // それ以外の場合は威力1で砲撃.
		else {
			fire(1);
		}
		// もう一度scanすることで連続攻撃を実装
		scan();
	}

	/**
	 * onHitByBullet: 弾丸に対して垂直に移動し，少し動く 
	 */
	public void onHitByBullet(HitByBulletEvent e) { //onHitByBulletのコンストラクタ
		turnRight(normalRelativeAngleDegrees(90 - (getHeading() - e.getHeading())));

	/**
         * getHeadingメソッドから取得した自機の向きの角度と
	 * HitByBulletEventのメソッド"getHeading"で命中した時点での弾丸の進行方向をそれぞれ取得。
	 * 自機の向きから弾丸の向きを引いた物を，さらに90度から引いて，この差をturnRightメソッドに渡す
	 */

		ahead(dist); // 先に要していたdistの値分前進する
		dist *= -1;  //distの値に(-1)を乗算する
		scan();  //もう一度scanを呼び,onScannnedRobotを呼び出しやすくする
	}

	/**
	 * onHitRobot:  Aim at it.  Fire Hard!
	 */
	public void onHitRobot(HitRobotEvent e) {
		double turnGunAmt = normalRelativeAngleDegrees(e.getBearing() + getHeading() - getGunHeading());

		turnGunRight(turnGunAmt);
		fire(3);
	}
}
