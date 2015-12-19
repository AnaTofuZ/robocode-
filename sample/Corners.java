/**
 * Copyright (c) 2001-2014 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 * 所謂コピーライトの表記。特に意味は無い
 */
package sample;


import robocode.DeathEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

//主要メソッドをimportする

import java.awt.*;

/**
 * Corners - a sample robot by Mathew Nelson.
 * <p/>
 * This robot moves to a corner, then swings the gun back and forth.
 *このロボットは角へ移動し，主砲を前後あちらこちらに動かす
 *
 * If it dies, it tries a new corner in the next round.
 *もしこのロボット自身が撃沈した場合，新しい角から次のラウンドを始める
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */

public class Corners extends Robot {
	int others; // Number of other robots in the game
	//このゲームで他のロボットの数を把握する
	static int corner = 0; // staticなint型変数cornerを宣言し0を代入
	// staticはround間で保存される
	boolean stopWhenSeeRobot = false; //boolean型変数stopWhenSeeRobotを宣言し，falseを代入
	// この一行がどう活用されるかはgo Corner()を参照

	/**
	 * run:  Corners' main run function.
	 * runメソッド: Cornerのmainとなるrunメソッド
	 */

	public void run() {
		// 機体の色を設定
		setBodyColor(Color.red);
		setGunColor(Color.black);
		setRadarColor(Color.yellow);
		setBulletColor(Color.green);
		setScanColor(Color.green);

		// robocode.Robotのメソッド，getOthersを用いて残っている敵の数をotherに渡す
		others = getOthers();

		// 角へ移動する
		goCorner();

		// 大砲の回転速度を3に初期化
		int gunIncrement = 3;
		//ここではint型変数gunIncrementを宣言し，そこに3を代入している

		// 大砲を行ったり来たり回転させる
		while (true) {
			for (int i = 0; i < 30; i++) { //カウンタ変数iが30になるまでループ
				turnGunLeft(gunIncrement);//先ほど宣言したgunIncrementをturnGunLeftに渡す。
			//此処では速さ3
			}
			gunIncrement *= -1; //gunIncrementの値に-1をかける。これで逆向きに回転する。
		}
	}

	/**
	 * goCorner: これはとても効率の悪い角へ向かうメソッドである。もっと良くしてください
	 */
	public void goCorner() {
		// 回転している時は自機は止まらない
		stopWhenSeeRobot = false;
		// 回転時に正面を向いた壁の右側へと移動する
		turnRight(normalRelativeAngleDegrees(corner - getHeading()));

		/*RobotクラスのturnRightメソッド(機体を右回転する)を使用
		 * turnRIghtに渡す値を正規化するためにnormalRelativeAngleDegreesメソッドを通して
		 *現在機体が向いている座標をgetHeadingメソッド取得し
		 *cornerからこれを引いた分の角度回転する
		 *turnRightメソッドは処理が終了するまで戻らないので回転中は自機は何もしない*/

		//stopWhenSeeRobot 変数をtrueにする

		stopWhenSeeRobot = true;

		//5000pixel分直進する。先ほど回転したので壁に当たる.
		//Robot内のaheadメソッドは壁に衝突すると動作を完了するので，衝突後turnLeftの動きに移行する
		ahead(5000);
		// 角に正面を向く
		turnLeft(90);
		// 角へと移動する
		ahead(5000);
		// 左に90度移動して動作完了
		turnGunLeft(90);
	}

	/**
	 * onScannedRobotメソッド:  静止して砲撃
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// 止まる動作を含むか，止まらずに撃つかの判断
		if (stopWhenSeeRobot) {
			//stopWhenSeeRobotがtrueだった時。すなわちgoCorner()で回転が終了した際は
			// stopメソッドを用いて動作を全て終了
			stop();
			// Call our custom firing method
			smartFire(e.getDistance());
			// Look for another robot.
			// NOTE:  If you call scan() inside onScannedRobot, and it sees a robot,
			// the game will interrupt the event handler and start it over
			scan();
			// We won't get here if we saw another robot.
			// Okay, we didn't see another robot... start moving or turning again.
			resume();
		} else {
			smartFire(e.getDistance());
		}
	}

	/**
	 * smartFire:  Custom fire method that determines firepower based on distance.
	 *
	 * @param robotDistance the distance to the robot to fire at
	 */
	public void smartFire(double robotDistance) {
		if (robotDistance > 200 || getEnergy() < 15) {
			fire(1);
		} else if (robotDistance > 50) {
			fire(2);
		} else {
			fire(3);
		}
	}

	/**
	 * onDeath:  We died.  Decide whether to try a different corner next game.
	 */
	public void onDeath(DeathEvent e) {
		// Well, others should never be 0, but better safe than sorry.
		if (others == 0) {
			return;
		}

		// If 75% of the robots are still alive when we die, we'll switch corners.
		if ((others - getOthers()) / (double) others < .75) {
			corner += 90;
			if (corner == 270) {
				corner = -90;
			}
			out.println("I died and did poorly... switching corner to " + corner);
		} else {
			out.println("I died but did well.  I will still use corner " + corner);
		}
	}
}
