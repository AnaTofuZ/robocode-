/**
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package sample;


import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;

public class Fire extends Robot{

	int dist = 50;
//被弾した時に動くための距離として50pixelを定義

	public void run(){

//色を設定

                setBodyColor(Color.orange);
                setGunColor(Color.orange);
                setRadarColor(Color.red);
                setScanColor(Color.red);
                setBulletColor(Color.red);

//ゆっくり右回転をし続ける

	while (true){
		turnGunRight(5);
	}
   }


     public void onScannedRobot(ScannedRobotEvent e){

// 他のロボットが接近，かつ，自機の体力が充分なとき
//高火力でfireをする

     if(e.getDistance() < 50 && get Energy() > 50){
	fire(3);
	} //もし体力が不十分，又はそこまで接近してない場合
	
	else{
		fire(1);
	}
	//もう一度呼ぶことで主砲が回転する前に撃つ事が可能
	scan();
   }


/**
*被弾時処理
* 進行方向に対し垂直な弾丸を撃つ
*/

	public void onHitByBullet(HitByBulletEvent e){

