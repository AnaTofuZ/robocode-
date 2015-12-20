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
import robocode.Condition;
import robocode.CustomEvent;

import java.awt.*;

//Target.javaを構成する上で必要なクラスをimport

/**
 * Target - a sample robot by Mathew Nelson.
 * <p/>
 * じっと座っている。エネルギーを20失った場合動く
 * このロボットはカスタムイベントのデモンストレーションである
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class Target extends AdvancedRobot { //AdvancedRobotから拡張

	int trigger; //int型変数を宣言 

	/**
	 * TrackFire's run メソッド
	 */
	public void run() {
		//色の設定
		//機体:白,砲身:白,レーダー:白 
		setBodyColor(Color.white);
		setGunColor(Color.white);
		setRadarColor(Color.white);

		//最初，自機が動く際はエネルギーが80の時 
		trigger = 80;
		//"trigger hit "という名前のカスタムイベントを生成 
		addCustomEvent(new Condition("triggerhit") {
			public boolean test() {
				return (getEnergy() <= trigger);
		//エネルギーを確認し，triggerの値以下ならtrueを返す
			}
		});
	}

	/**
	 * カスタムイベント発生時に呼び出されるメソッド
	 */
	public void onCustomEvent(CustomEvent e) {
		//このカスタムイベントはtrigger hitと呼ばれる 
		if (e.getCondition().getName().equals("triggerhit")) { 
			//もし発生したコンディションがtriggerhitと呼ばれるものなら
			// triggerの値を調整するか
			// 砲撃イベントを何度も行う
			trigger -= 20;
			//triggerの値から-20引く。

			out.println("Ouch, down to " + (int) (getEnergy() + .5) + " energy.");
			// getEnergyで入手した数値+0.5を現在のエネルギーとして出力
			turnLeft(65); //左65度回転
			ahead(100); //100ピクセル直進
		}
	}
}
