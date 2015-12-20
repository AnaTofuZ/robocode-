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
import static robocode.util.Utils.normalAbsoluteAngle;
import static robocode.util.Utils.normalRelativeAngle;

import java.awt.*;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

//Interactiveを構成する上で必要なクラスをimportする

/**
 * Interactive - a sample robot by Flemming N. Larsen.
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
 * <p/>
 * Note that the robot will continue firing as long as the mouse button is
 * pressed down.
 * <p/>
 * By enabling the "Paint" button on the robot console window for this robot,
 * a cross hair will be painted for the robots current aim (controlled by the
 * mouse).
 *
 * @author Flemming N. Larsen (original)
 *
 * @version 1.2
 *
 * @since 1.3.4
 */
public class Interactive extends AdvancedRobot { //AdvancedRobotを継承したInteractiveメソッド

	//移動指示       : 1 = 前進			0 = そのまま			-1 = 後退
	int moveDirection; //int 型変数moveDirectionを宣言

	//回転指示	:		1= 右回転 		0 = 回転しない	-1 = 左回転
	int turnDirection;

	//移動時進んだ分の距離(ピクセル)の合計
	double moveAmount;

	//目標の(x,y)座標と連携
	int aimX, aimY;

	//火力。			もし0なら砲撃しない
	int firePower;

	//robotを実行するにはrunメソッドを実行しなければならない
	public void run() {

		//ロボットの機体の色をコントロールする
		//ボディを黒色, 砲身を白色, レーダーを赤色
		setColors(Color.BLACK, Color.WHITE, Color.RED);

		// 永遠ループ
		for (;;) {
			//ロボットを前進，後退，もしくは止まることができるように設定を行う
			//移動方向とマウス移動時におけるピクセルの合計の乗算で前進を設定する
			setAhead(moveAmount * moveDirection);

			//残り移動距離が0ピクセルに近づくいくようにデクリメント
			//もしマウスホイールが止まれば自動でロボットが停止
			//回転も終了
			moveAmount = Math.max(0, moveAmount - 1);
			//mathクラスのmaxメソッドで0かmoveAmount-1の多い方の値をmoveAmountに渡す

			//右回転，及び左回転(共に最大スピード)もしくは
			//回転方向の操作で回転を止めるかを決定
			setTurnRight(45 * turnDirection); // degrees
			//45にturnDirectionの値を乗算(turnDirectionは±1か0なので45をかけ，45度回転)
			//setメソッドなので実行動作が行われるまで待機

			//マウスの(x,y)座標と現在の自機の(x,y)座標を持って弾丸の目標点を計算

			double angle = normalAbsoluteAngle(Math.atan2(aimX - getX(), aimY - getY()));
			//double型変数 angleにmathクラスのatan2を用いて角度θ(シータ)を返す

			setTurnGunRightRadians(normalRelativeAngle(angle - getGunHeadingRadians()));
			//angleから現在の大砲の向きの絶対角度を引いた値分右回転
			//これもsetメソッドなので実行動作が行われるまで待機

			//fireパワーが0ではない限り設定された分の砲撃を行う
			if (firePower > 0) {
				setFire(firePower);
			}

			//execute()メソッドを用いて今まで待機していた全てのsetメソッドを開始
			execute();

			//次のループに突入
		}
	}

	//キーボードから何かが入力されればこのメソッドが呼び出される 
	public void onKeyPressed(KeyEvent e) { //keyPresssedのオーバーライド
		switch (e.getKeyCode()) { //押されたキーによってスイッチ分岐
		case VK_UP://上矢印
		case VK_W: //wキー
			//キーが入力され続ければ永続的に前進を行う 
			moveDirection = 1; //変数moveDirctionに1を代入
			moveAmount = Double.POSITIVE_INFINITY; //変数moveAmoutにdouble型のPSITIVE_INFINITYを収納
			break;

		case VK_DOWN://下矢印
		case VK_S://sキー
			//キーが入力され続ければ永続的に後進を行う 
			moveDirection = -1;//変数moveDirectionに-1を代入
			moveAmount = Double.POSITIVE_INFINITY; //moveAmountにPOSITIVE_INFINITYを収納
			break;

		case VK_RIGHT://右矢印
		case VK_D://dキー
			// 右方向に回転 
			turnDirection = 1;
			break;

		case VK_LEFT:
		case VK_A:
			// 左方向に回転 
			turnDirection = -1;
			break;
		}
	}

	//キーボードから手を話したらこのメソッドが呼び出される 
	public void onKeyReleased(KeyEvent e) { //keyReleasedメソッドのオーバーライド
		switch (e.getKeyCode()) { //e.getKeyCodeメソッド(押されていたキー)によって分岐 
		case VK_UP:
		case VK_W:
		case VK_DOWN:
		case VK_S:
			//上下矢印キー，W，Sキーの場合
			//先ほどまで進んでいた方向を向いたま停止 
			moveDirection = 0;
			moveAmount = 0;  //moveDirection及びmovrAmountに0を代入してbreak
			break;

		case VK_RIGHT:
		case VK_D:
		case VK_LEFT:
		case VK_A:
			//左右矢印キー，D,Aキーの場合
			//回転を停止 
			turnDirection = 0;
			break;
		}
	}

	//マウスホイールが回転し始めたらこのメソッドが呼び出される 
	public void onMouseWheelMoved(MouseWheelEvent e) { //MouseWheelEvetクラスからイベントを読みこむ
		
		//変数moveDirectionにマウスホイールを回転させたクリック数を返す
		//この際マウスホイールが上側に回転した場合は負の値が返される。下側に回転した場合は正の値

		moveDirection = (e.getWheelRotation() < 0) ? 1 : -1;

		//moAmountにgetWheelRotationの値に5をかけた値を渡す(ここで5は適当に決定された。大きいほど早く回転する)

		moveAmount += Math.abs(e.getWheelRotation()) * 5;
	}

	//マウスが移動した場合にこのメソッドが呼び出される
 
	public void onMouseMoved(MouseEvent e) {
		// 目標の設定はマウスポインタの(x,y)座標と同期 
		aimX = e.getX();
		aimY = e.getY();
	}

	//マウスのボタンが押された際このメソッドが呼び出される 
	public void onMousePressed(MouseEvent e) { 
		if (e.getButton() == MouseEvent.BUTTON3) {
			//ボタン3:威力3の砲撃:色は赤色
			firePower = 3;
			setBulletColor(Color.RED);
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			//ボタン2:威力2の砲撃:色はオレンジ 
			firePower = 2;
			setBulletColor(Color.ORANGE);
		} else {
			//ボタン1，もしくは定義されていないボタン 
			//威力1の砲撃:色は黄色
			firePower = 1;
			setBulletColor(Color.YELLOW);
		}
	}

	//マウスボタンから手が離れた場合呼び出されるメソッド 
	public void onMouseReleased(MouseEvent e) {
		// 0の威力で砲撃(撃たない) 
		firePower = 0;
	}

	//paintボタンを押すことでエイムを表示
	//ペイントボタンはバトルフィールド上でクリックすることができる	

	public void onPaint(Graphics2D g) {
		//現在のエイム範囲を赤色のサークルで表示する 
	
		g.setColor(Color.RED);
		g.drawOval(aimX - 15, aimY - 15, 30, 30);
		g.drawLine(aimX, aimY - 4, aimX, aimY + 4);
		g.drawLine(aimX - 4, aimY, aimX + 4, aimY);
	}
}
