/**
 * Copyright (c) 2001-2014 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 * 所謂コピーライト
 */
package sample;


import robocode.JuniorRobot;

//MyFirstRobotを構成する上で使用するメソッドが含まれているクラスをimportする

/**
 * MyFirstJuniorRobot - a sample robot by Flemming N. Larsen
 * <p/>
 * シーソーの様な動きをとる。もし敵機を検知出来なければ砲身を振り回す
 * もし，ロボットが見つかれば周り，そして砲撃を行う
 *
 * @author Flemming N. Larsen (original)
 */
public class MyFirstJuniorRobot extends JuniorRobot { //JuniorRobotを継承

	/**
	 * MyFirstJuniorRobot's run メソッド - シーソーの様な動きがデフォルト
	 */
	public void run() {
		//色の設定
		//機体:緑 主砲:黒 レーザー:青 
		setColors(green, black, blue);

		//永遠にシーソー 
		while (true) {
			ahead(100); //100ピクセル前進 
			turnGunRight(360); // 砲身を右に360度回転
			back(100); // 100ピクセル後進
			turnGunRight(360); // 砲身を左に360度回転
		}
	}

	/**
	 *もし敵機を見つけた場合，そちらを向いて砲撃 
	 */
	public void onScannedRobot() {
		//scanした敵機の方に砲身を向ける 
		turnGunTo(scannedAngle);

		//威力1で砲撃
		fire(1);
	}

	/**
	 *弾があたった場合，弾に垂直に成るように回転 
	 *そうすればシーソーの動きで弾丸を避けれるかもしれない 
	 */
	public void onHitByBullet() {
		// Move ahead 100 and in the same time turn left papendicular to the bullet
		
		turnAheadLeft(100, 90 - hitByBulletBearing);
		
		//JuniorRobot内のメソッドturnAheadLeftを用いて100ピクセル前進しながら,
		//90度からhitByBulletBearingで弾丸から機体の角度を引いた値左回転する
	}
}
