/**
 * Copyright (c) 2001-2014 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 * 所謂コピーライト
 */
package sample;

//SittingDuckが収録されているpackage名

import robocode.AdvancedRobot;
import robocode.RobocodeFileOutputStream;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

//構成するために必要なメソッドが含まれているクラスをimportする

/**
 * SittingDuck - a sample robot by Mathew Nelson.
 * <p/>
 * とくに何もしないで静止している。このロボットは粘り強さを示したデモンストレーション機である。
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 * @author Andrew Magargle (contributor)
 */

public class SittingDuck extends AdvancedRobot {	//AdvancedRobotを継承
	static boolean incrementedBattles = false;	//staticなboolean型変数incrementedBattlesを宣言し，falseを代入

	public void run() {
		
		setBodyColor(Color.yellow);
		setGunColor(Color.yellow);
				//機体の色設定。機体と砲身が黄色

		int roundCount, battleCount;

			//int型変数，roundCount，battleCountをそれぞれ宣言

		try { //try処理開始。例外処理が発生したらcatchされる 
			BufferedReader reader = null;	//BufferedReader readerにnullを入れる(例外処理)
			try {
				//  "count.dat"count.datファイル(ラウンドcountとバトルcountを含む)
				//を読み出すことを試行 
			
				reader = new BufferedReader(new FileReader(getDataFile("count.dat")));

				// countの値を受け取ることを試みる
				roundCount = Integer.parseInt(reader.readLine());
				battleCount = Integer.parseInt(reader.readLine());

			} finally { //finally内部はいつでも処理される
				if (reader != null) {
					reader.close();	 //もしreaderがnullでないなら，reader.close()メソッドを呼び出す
				}
			}
		} catch (IOException e) {
			//ファイルの読み込みができなかった場合,両者のcountを0にする 
			roundCount = 0;
			battleCount = 0;
		} catch (NumberFormatException e) {
			// 同じくファイルの読み込みができなかった場合，両者のcountを0にする
			roundCount = 0;
			battleCount = 0;
		}

		// ラウンド終了時にインクリメント
		roundCount++;

		//もし先ほどインクリメントするのを忘れていたら 
		//メンバ変数は別に今までどおり有効となっている	
		if (!incrementedBattles) { 
			// バトルに付き#の値をインクリメント
			battleCount++;
			incrementedBattles = true;
		}

		PrintStream w = null;
		try {
			w = new PrintStream(new RobocodeFileOutputStream(getDataFile("count.dat")));

			w.println(roundCount);
			w.println(battleCount);

			// PrintStreamsはエラー処理を出さない。フラグ設定のみ行うので詳しくはここ参照 
			if (w.checkError()) {
				out.println("I could not write the count!");
			}
		} catch (IOException e) {
			out.println("IOException trying to write: ");
			e.printStackTrace(out);
		} finally {
			if (w != null) {
				w.close();
			}
		}
		out.println("I have been a sitting duck for " + roundCount + " rounds, in " + battleCount + " battles."); 
	}
}
