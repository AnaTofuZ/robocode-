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

 //RamFire.javaを構築する上で必要なクラスをimport

/**
 * RamFire - a sample robot by Mathew Nelson.
 * <p/>
 * ドライバーは他のロボットの衝突をしようとする
 * もし衝突したら砲撃を行う 
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class RamFire extends Robot { //Robotクラスから継承
	int turnDirection = 1; //時計回りか反時計回り 
				//int型変数turnDirectionを宣言し1を代入

	/**
	 * run:そのあたりを回転してロボットを見つける 
	 */
	public void run() {
		//色の設定 機体:黄緑 砲身:グレー レーダー:ダークグレー 
		setBodyColor(Color.lightGray);
		setGunColor(Color.gray);
		setRadarColor(Color.darkGray);

		while (true) { //永遠ループ
			turnRight(5 * turnDirection); //turnDirectionの値に5をかけた分だけ右回転
		}
	}

	/**
	 * onScannedRobot: 他のロボットを発見したら突撃 
	 */
	public void onScannedRobot(ScannedRobotEvent e) {

		if (e.getBearing() >= 0) { //敵機との相対角度が0度以上なら
			turnDirection = 1; //turnDirectionに1を代入
		} else {		   //もし0度未満であるならば
			turnDirection = -1;//-1を代入
		}

		turnRight(e.getBearing()); //敵機の方向に右回転
		ahead(e.getDistance() + 5); //敵機との相対距離+5ピクセル前進
		scan(); // scanする事でもう一度前進する可能性
	}

	/**
	 * onHitRobotメソッド: 敵機と向かい合って,最大火力で砲撃し,再び体当たり
	 */
	public void onHitRobot(HitRobotEvent e) {
		if (e.getBearing() >= 0) { //敵機との相対角度が0度以上なら
			turnDirection = 1;//turnDirectionに1を代入
		} else {		//もし角度が0度未満なら
			turnDirection = -1; //-1をturnDirectionに代入
		}
		turnRight(e.getBearing()); //相対角度の分だけ右回転

		//砲撃ではなく体当たりで敵機を撃破すればボーナスポイントが手に入るので 
		//このロボットでは体当たりで止めを指す 
		if (e.getEnergy() > 16) { //もし，敵機のエナジーが16以上なら
			fire(3);	//威力3で砲撃
		} else if (e.getEnergy() > 10) {	//もし，10以上なら
			fire(2);	//威力3で砲撃
		} else if (e.getEnergy() > 4) {	//もし，4以上なら
			fire(1);	//威力1で砲撃
		} else if (e.getEnergy() > 2) {	//もし，2以上なら
			fire(.5);	//0.5で砲撃
		} else if (e.getEnergy() > .4) { //もし，0.4以上なら
			fire(.1);	//威力0.1で砲撃
		}
		ahead(40); //そして突撃
	}
}
