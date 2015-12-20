/**
 * Copyright (c) 2001-2014 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package sample;


import robocode.HitByBulletEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

import java.awt.*;


/**
 * Painting RobotはonPaint()メソッドと,getGraphics()メソッドのデモンストレーション
 * Also demonstrate feature of debugging properties on RobotDialog
 * <p/>
 * 基本的にはシーソーのような動きを行い，各メソッドの最後に回転する 
 *paintingを使用した際，自機の周囲に円を作る 
 *
 * @author Stefan Westen (original SGSample)
 * @author Pavel Savara (contributor)
 * 所謂コピーライト
 */

public class PaintingRobot extends Robot { //Robotclassから継承

	/**
	 * PaintingRobot's run method - シーソー動作
	 */
	public void run() {
		while (true) {//永遠ループ
			ahead(100); //100ピクセル前進
			turnGunRight(360); //360度砲身を右回転
			back(100); //100ピクセル後退
			turnGunRight(360); //360度砲身を右回転
		}
	}

	/**
	 *敵機を発見した際，砲撃 
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		//ロボットダイアログのデバッグプロパティの機能のデモンストレーション 
		setDebugProperty("lastScannedRobot", e.getName() + " at " + e.getBearing() + " degrees at time " + getTime());
		/**
		  *setDebugPropertyはStringを2つ受け取る	
		  *2つめのstringにはe.getNameメソッドでscanしたロボット名
		  *getBearingで相対速度，getTimeでゲーム時間をそれぞれ文字列として渡す
		  */
		fire(1);//威力1で砲撃
	}

	/**
	 * 被弾時には，弾丸に垂直になるように動く
	 * 上手く行けば永遠に弾丸からシーソーの動きで避けることができるかもしれない 
	 *追加で，被弾したらオレンジの円を出力する 
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		//ロボットダイアログ上のデバッグプロパティの特徴となる機能を示す 
		setDebugProperty("lastHitBy", e.getName() + " with power of bullet " + e.getPower() + " at time " + getTime());

 		  /**
                    *setDebugPropertyはStringを2つ受け取る        
                    *2つめのstringにはe.getNameメソッドでscanしたロボット名
                    *getPowerで被弾した弾丸のパワーを
		    *getTimeでゲーム時間をそれぞれ文字列として渡す
                    */


		setDebugProperty("lastScannedRobot", null);

		//デバッグプロパティを削除

		//先頭表示(バトル・ビュー)をペイントすることでデバックを行う
		Graphics2D g = getGraphics();

		g.setColor(Color.orange);
		g.drawOval((int) (getX() - 55), (int) (getY() - 55), 110, 110);
		g.drawOval((int) (getX() - 56), (int) (getY() - 56), 112, 112);
		g.drawOval((int) (getX() - 59), (int) (getY() - 59), 118, 118);
		g.drawOval((int) (getX() - 60), (int) (getY() - 60), 120, 120);

		//90度から敵機との相対角度を引いた分左回転
		turnLeft(90 - e.getBearing());
	}

	/**
	  * paintingrobotの周辺に赤い円を作る
	  */
	public void onPaint(Graphics2D g) {
		g.setColor(Color.red);
		g.drawOval((int) (getX() - 50), (int) (getY() - 50), 100, 100);
		g.setColor(new Color(0, 0xFF, 0, 30));
		g.fillOval((int) (getX() - 60), (int) (getY() - 60), 120, 120);
	}
}
