/**
 * Copyright (c) 2001-2014 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package sample;


import robocode.AdvancedRobot;
import robocode.util.Utils;
import static robocode.util.Utils.normalAbsoluteAngle;
import static robocode.util.Utils.normalRelativeAngle;

import java.awt.*;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashSet;
import java.util.Set;

//Interactive_v2上で使用するメソッドの為に必要なクラスをimportする

/**
 * Interactive_v2 - a modified version of the sample robot Interactive by Flemming N. Larsen
 *                  to use absolute movements (up, right, down, left) by Tuan Anh Nguyen.
 * <p/>
 * このロボットはマウスとKeyboardのみでコントロールを行う
 * <p/>
 * キーボード入力:
 * - W 又は 上矢印:   前進する
 * - S 又は 下矢印:   後進する
 * - A 又は 右矢印:   右回転
 * - D 又は 左矢印:   左回転
 * マウス入力:
 * - 移動:     動きを追って，銃が移動
 * - 上にホイール:    前進
 * - 下ホイール:      後進
 * - ボタン 1:    砲撃をこの大きさで行う = 1
 * - ボタン 2:    砲撃をこの大きさで行う = 2
 * - ボタン 3:    砲撃をこの大きさで行う = 3
 * <p/>
 * 砲撃の威力に応じて弾丸の色を変更:
 * - Power = 1:   黄色
 * - Power = 2:   オレンジ
 * - Power = 3:   赤
 * Note that the robot will continue firing as long as the mouse button is
 * pressed down.
 * <p/>
 * By enabling the "Paint" button on the robot console window for this robot,
 * a cross hair will be painted for the robots current aim (controlled by the
 * mouse).
 *
 * @author Flemming N. Larsen (original)
 * @author Tuan Anh Nguyen (contributor)
 *
 * @version 1.0
 *
 * @since 1.7.2.2
 */
public class Interactive_v2 extends AdvancedRobot {

	//エイムの(x,y)座標を宣言 
	int aimX, aimY;

	//fireパワーを宣言。firePower=0は撃たないことを意味する。 
	int firePower;

	//スクリーン上での方向を列挙型enumでDirectionの中に宣言 
	private enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}

	//現在の移動方向
	//final修飾子を使った為，以下set<Direction> directionは変更することが出来ない
	private final Set<Direction> directions = new HashSet<Direction>();

	//robocode上でのメインメソッドの様なrunメソッド 
	public void run() {

		//色の設定 
		//機体:黒,銃身:白,レーザー:赤色
		setColors(Color.BLACK, Color.WHITE, Color.RED);

		//永遠ループ 
		for (;;) {
			//機体が動こうとするときdistanceToMoveと距離は同じとみなす
			//setAheadメソッドを用いてdistanceToMoveの値の分前進をさせる待機命令
 
			setAhead(distanceToMove());

			//ボディーを回転させることによって正しい方向を示させる 
			setTurnRight(angleToTurnInDegrees());

			// 銃身とエイムの(x,y)座標をマウスポインタによって決定させる 

			double angle = normalAbsoluteAngle(Math.atan2(aimX - getX(), aimY - getY()));
			//double型変数angleにaimX,Yから現在のロボットのx,y座標を引いた絶対角度を渡す

			setTurnGunRightRadians(normalRelativeAngle(angle - getGunHeadingRadians()));
			
			//ラジアン角度でangleから現在主砲が向いている角度を引いた分だけ主砲を右回転

			//砲撃は下記で設定した特殊な砲撃を行う。尚0は撃たないことを意味する 
			if (firePower > 0) {
				setFire(firePower);
			}

			//execute()メソッドを用いて全ての待機された命令を開始する 
			execute();

			// 次のループを再開させる 
		}
	}

	// キーボード入力がされた時に呼び出されるメソッド 
	public void onKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) { //押されたキーによって分岐
		case VK_UP:
		case VK_W:   //上矢印キー，Wキーの場合
			directions.add(Direction.UP); //Directionで定義したUPをdirectionsに追加
			break;

		case VK_DOWN:
		case VK_S:  //下矢印キー，Sキーの場合
			directions.add(Direction.DOWN); //Directionで定義したDOWNをdirectionsに追加
			break;

		case VK_RIGHT:
		case VK_D:  //右矢印キー，Dキーの場合
			directions.add(Direction.RIGHT); //DirectionのRIGHTをdirectionsに追加
			break;

		case VK_LEFT:
		case VK_A://左キー，Aキーの場合
			directions.add(Direction.LEFT); //DirectionのLEFTをdirectionsに追加
			break;
		}
	}

	// キーボードから指を離した際に呼び出されるメソッド
	 
	public void onKeyReleased(KeyEvent e) {
		switch (e.getKeyCode()) { //押されていたキーで分岐
		case VK_UP:
		case VK_W:
			directions.remove(Direction.UP);//上キー，Wキーの場合directionsからUPの要素をリムーブする
			break;

		case VK_DOWN:
		case VK_S:
			directions.remove(Direction.DOWN); //下キー，Sキーの場合，directionsからDOWNの要素をリムーブする
			break;

		case VK_RIGHT:
		case VK_D:
			directions.remove(Direction.RIGHT);//右キー，Dキーの場合，directionsからRIGHTの要素をリムーブする
			break;

		case VK_LEFT:
		case VK_A:
			directions.remove(Direction.LEFT);//左キー，Aキーの場合，directionsからLEFTの要素をリムーブする
			break;
		}
	}

	//マウスのホイールが動いた時に呼び出されるメソッド 
	public void onMouseWheelMoved(MouseWheelEvent e) {//特に意味は無い
	}

	//マウスが動いた際に呼び出されるメソッド 
	public void onMouseMoved(MouseEvent e) {
		//エイムの値を現在マウスが指している値にする 
		aimX = e.getX();
		aimY = e.getY();
	}

	//マウスのボタンが押された際に呼び出されるメソッド 
	public void onMousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			//3ボタン : 威力3の砲撃 色は赤色 
			firePower = 3;
			setBulletColor(Color.RED);
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			 //2ボタン : 威力2の砲撃 色はオレンジ 
			firePower = 2;
			setBulletColor(Color.ORANGE);
		} else {
			// 1ボタンかそれ以外のボタンが押された時
			//威力1の砲撃 色は黄色 
			firePower = 1;
			setBulletColor(Color.YELLOW);
		}
	}

	//マウスのボタンから指が離された時に呼び出されるメソッド 
	public void onMouseReleased(MouseEvent e) {
		// Fire power = 0は撃たないことを意味する。
		firePower = 0;
	}

	//robotコンソールからこの機体はペイントボタンを押す事ができる。
	//押すとペイント機能(エイム表示)をすることが可能 

	public void onPaint(Graphics2D g) {

		//エイム範囲を赤色のサークルとして表示
	
		g.setColor(Color.RED);
		g.drawOval(aimX - 15, aimY - 15, 30, 30);
		g.drawLine(aimX, aimY - 4, aimX, aimY + 4);
		g.drawLine(aimX - 4, aimY, aimX + 4, aimY);
	}

	// アングルを戻した時発生する微小区間を決定し，その方向にロボットを剥ける。
	private double angleToTurnInDegrees() {
		if (directions.isEmpty()) {
			return 0;	//directionが空だった場合0を返す
		}
		return Utils.normalRelativeAngleDegrees(desiredDirection() - getHeading());
			//disiredDirection()メソッドを呼び出し，そこから帰ってきた値とrobotの現在向いている角度を引いた者を返す
	}

	// 移動距離を返すメソッド
	private double distanceToMove() {
		//既にdirectionsの値が空になってしまっていた場合，0を返す 
		if (directions.isEmpty()) {
			return 0;
		}
		//もしangleToTurnInDegreesが45より大きければ5ピクセルのみ動かす 
		if (Math.abs(angleToTurnInDegrees()) > 45) {
			return 5;
		}
		//それ以外ならフルスピードで移動する 
		return Double.POSITIVE_INFINITY;
	}

	//2つ以上の移動キーが入力された場合，斜め移動を行う
 
	private double desiredDirection() {
		if (directions.contains(Direction.UP)) {	//もしUPが収納されていて
			if (directions.contains(Direction.RIGHT)) {//右移動が支持されたら
				return 45;	//45を返す
			}
			if (directions.contains(Direction.LEFT)) {	//左移動が指示されたら
				return 315;	//315が返される
			}
			return 0;//ただUPだけなら0を返す
		}
		if (directions.contains(Direction.DOWN)) {	//もしDOWNが収納されていて
			if (directions.contains(Direction.RIGHT)) {	//右移動が指示されたら	
				return 135;	//135を返す	
			}
			if (directions.contains(Direction.LEFT)) {	//左移動ならば
				return 225;	//225を返す
			}
			return 180; 	//それ以外は180を返す	
		}
		if (directions.contains(Direction.RIGHT)) {
			return 90;	//右移動のみだったら90度を返す
		}
		if (directions.contains(Direction.LEFT)) {	//左移動のみなら270を返す
			return 270;
		}
		return 0;	//それ以外なら0を返す
	}
}
