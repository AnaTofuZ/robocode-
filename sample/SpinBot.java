/**
 * Copyright (c) 2001-2014 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 * 所謂コピーライト
 */

package sample;


import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;

//SpinBotを構成するためにimportする

/**
 * SpinBot - a sample robot by Mathew Nelson.
 * <p/>
 * 円運動を行い，敵機を見つけ時に砲撃する
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class SpinBot extends AdvancedRobot { //AdvancedRobotを継承

	/**
	 * SpinBot's run メソッド - 円運動
	 */
	public void run() {
		//色の設定
		//機体:青,砲身;青,レーダー,黒，;scan:黄色 
		setBodyColor(Color.blue);
		setGunColor(Color.blue);
		setRadarColor(Color.black);
		setScanColor(Color.yellow);

		//永久ループ 
		while (true) {
			//ゲーム開始時に自由に動けるかどうかを問い合わせる
			// このロボットは右回転をよくする予定
			setTurnRight(10000);
			//setTurnRightメソッドを用いて10000度右回転させるように待機

			// ロボットのSPEEDを5に制限

			setMaxVelocity(5);

			/**
		          *前進を始める。
			  *先ほど右回転を待機させていたので，前進と同時に
			  *右回転が始まる
			  */
			ahead(10000);
			// 繰り返し.
		}
	}

	/**
	 * onScannedRobot時のメソッド:高火力での砲撃!
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		fire(3);//威力3での砲撃
	}

	/**
	 * onHitRobot: もしダメなら回転と前進がストップする 
	 * そうなった場合，回転を維持しながら回って脱出
	 */
	public void onHitRobot(HitRobotEvent e) { //敵機にぶつかった場合のイベント
		if (e.getBearing() > -10 && e.getBearing() < 10) { //相対角度が-10度より上で，10度未満であるならば
			fire(3);	//威力3で砲撃
		}
		if (e.isMyFault()) {	//もし自分から突撃していれば
			turnRight(10); //10度右回転
		}
	}
}
