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
import robocode.Robot;
import robocode.ScannedRobotEvent;

//MyFirstRobotを構成する上で使用するメソッドが収納されているクラスをimport

/**
 * MyFirstRobot - a sample robot by Mathew Nelson.
 * <p/>
 * シーソーの様な動きを行う。各メソッドの最後に砲身を回転させる 
 *
 * @author Mathew A. Nelson (original)
 */
public class MyFirstRobot extends Robot { //Robotから継承

	/**
	 * MyFirstRobot's run メソッド - シーソーみたいな動き
	 */
	public void run() {

		while (true) {
			ahead(100); // 100ピクセル前進
			turnGunRight(360); // 砲身を右に360度回転
			back(100); // 100ピクセル後進
			turnGunRight(360); // 砲身を360度右回転
		}
	}

	/**
	 *敵機を見つけた場合攻撃 
	 */
	public void onScannedRobot(ScannedRobotEvent e) { //onScannnedRobotをオーバーライド
		fire(1); //威力1で攻撃
	}

	/**
	 *もし被弾した場合，弾丸と直角に回転 
	 *おそらくこのシーソーの動きをすれば永久的に避けることができるだろう 
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		turnLeft(90 - e.getBearing());//被弾時の位置を90度から引いて，左回転
	}
}												

