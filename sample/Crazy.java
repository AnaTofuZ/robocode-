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
			  *waitForメソッドは条件設定したTurnCompleteConditionクラスが完了した事を読み取るまで戻されない
			  *TurnCompleteConditionは回転が完了したかどうかを判断するクラスなので
			  *setTurnRight(90)が完了するまで保持される。
		       	  */

			setTurnLeft(180);

			//setTurnLeftメソッドに数値180を渡す。動きはsetTurnRightメソッドの逆で左回転する

	
			waitFor(new TurnCompleteCondition(this));

			//先ほどと同様にwaitForメソッドを読み込んだ瞬間に左回転が開始される

			setTurnRight(180);

			//setTurnRightメソッドに180の数値を渡す

			waitFor(new TurnCompleteCondition(this));
			// 同様にwaitForメソッドで確認を取る。ここまでがループされる
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
