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

	// Called when a key has been pressed
	public void onKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case VK_UP:
		case VK_W:
			// Arrow up key: move direction = forward (infinitely)
			moveDirection = 1;
			moveAmount = Double.POSITIVE_INFINITY;
			break;

		case VK_DOWN:
		case VK_S:
			// Arrow down key: move direction = backward (infinitely)
			moveDirection = -1;
			moveAmount = Double.POSITIVE_INFINITY;
			break;

		case VK_RIGHT:
		case VK_D:
			// Arrow right key: turn direction = right
			turnDirection = 1;
			break;

		case VK_LEFT:
		case VK_A:
			// Arrow left key: turn direction = left
			turnDirection = -1;
			break;
		}
	}

	// Called when a key has been released (after being pressed)
	public void onKeyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case VK_UP:
		case VK_W:
		case VK_DOWN:
		case VK_S:
			// Arrow up and down keys: move direction = stand still
			moveDirection = 0;
			moveAmount = 0;
			break;

		case VK_RIGHT:
		case VK_D:
		case VK_LEFT:
		case VK_A:
			// Arrow right and left keys: turn direction = stop turning
			turnDirection = 0;
			break;
		}
	}

	// Called when the mouse wheel is rotated
	public void onMouseWheelMoved(MouseWheelEvent e) {
		// If the wheel rotation is negative it means that it is moved forward.
		// Set move direction = forward, if wheel is moved forward.
		// Otherwise, set move direction = backward
		moveDirection = (e.getWheelRotation() < 0) ? 1 : -1;

		// Set the amount to move = absolute wheel rotation amount * 5 (speed)
		// Here 5 means 5 pixels per wheel rotation step. The higher value, the
		// more speed
		moveAmount += Math.abs(e.getWheelRotation()) * 5;
	}

	// Called when the mouse has been moved
	public void onMouseMoved(MouseEvent e) {
		// Set the aim coordinate = the mouse pointer coordinate
		aimX = e.getX();
		aimY = e.getY();
	}

	// Called when a mouse button has been pressed
	public void onMousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			// Button 3: fire power = 3 energy points, bullet color = red
			firePower = 3;
			setBulletColor(Color.RED);
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			// Button 2: fire power = 2 energy points, bullet color = orange
			firePower = 2;
			setBulletColor(Color.ORANGE);
		} else {
			// Button 1 or unknown button:
			// fire power = 1 energy points, bullet color = yellow
			firePower = 1;
			setBulletColor(Color.YELLOW);
		}
	}

	// Called when a mouse button has been released (after being pressed)
	public void onMouseReleased(MouseEvent e) {
		// Fire power = 0, which means "don't fire"
		firePower = 0;
	}

	// Called in order to paint graphics for this robot.
	// "Paint" button on the robot console window for this robot must be
	// enabled in order to see the paintings.
	public void onPaint(Graphics2D g) {
		// Draw a red cross hair with the center at the current aim
		// coordinate (x,y)
		g.setColor(Color.RED);
		g.drawOval(aimX - 15, aimY - 15, 30, 30);
		g.drawLine(aimX, aimY - 4, aimX, aimY + 4);
		g.drawLine(aimX - 4, aimY, aimX + 4, aimY);
	}
}
