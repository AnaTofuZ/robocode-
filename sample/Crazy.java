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

			// Tell the game we will want to turn right 90
			setTurnRight(90);
			// At this point, we have indicated to the game that *when we do something*,
			// we will want to move ahead and turn right.  That's what "set" means.
			// It is important to realize we have not done anything yet!
			// In order to actually move, we'll want to call a method that
			// takes real time, such as waitFor.
			// waitFor actually starts the action -- we start moving and turning.
			// It will not return until we have finished turning.
			waitFor(new TurnCompleteCondition(this));
			// Note:  We are still moving ahead now, but the turn is complete.
			// Now we'll turn the other way...
			setTurnLeft(180);
			// ... and wait for the turn to finish ...
			waitFor(new TurnCompleteCondition(this));
			// ... then the other way ...
			setTurnRight(180);
			// .. and wait for that turn to finish.
			waitFor(new TurnCompleteCondition(this));
			// then back to the top to do it all again
		}
	}

	/**
	 * onHitWall:  Handle collision with wall.
	 */
	public void onHitWall(HitWallEvent e) {
		// Bounce off!
		reverseDirection();
	}

	/**
	 * reverseDirection:  Switch from ahead to back & vice versa
	 */
	public void reverseDirection() {
		if (movingForward) {
			setBack(40000);
			movingForward = false;
		} else {
			setAhead(40000);
			movingForward = true;
		}
	}

	/**
	 * onScannedRobot:  Fire!
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		fire(1);
	}

	/**
	 * onHitRobot:  Back up!
	 */
	public void onHitRobot(HitRobotEvent e) {
		// If we're moving the other robot, reverse!
		if (e.isMyFault()) {
			reverseDirection();
		}
	}
}
