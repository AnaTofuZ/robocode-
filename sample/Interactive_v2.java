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

	// Called when a key has been pressed
	public void onKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case VK_UP:
		case VK_W:
			directions.add(Direction.UP);
			break;

		case VK_DOWN:
		case VK_S:
			directions.add(Direction.DOWN);
			break;

		case VK_RIGHT:
		case VK_D:
			directions.add(Direction.RIGHT);
			break;

		case VK_LEFT:
		case VK_A:
			directions.add(Direction.LEFT);
			break;
		}
	}

	// Called when a key has been released (after being pressed)
	public void onKeyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case VK_UP:
		case VK_W:
			directions.remove(Direction.UP);
			break;

		case VK_DOWN:
		case VK_S:
			directions.remove(Direction.DOWN);
			break;

		case VK_RIGHT:
		case VK_D:
			directions.remove(Direction.RIGHT);
			break;

		case VK_LEFT:
		case VK_A:
			directions.remove(Direction.LEFT);
			break;
		}
	}

	// Called when the mouse wheel is rotated
	public void onMouseWheelMoved(MouseWheelEvent e) {// Do nothing
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

	// Returns the angle to turn, which is the delta between the desired
	// direction and the current heading of the robot
	private double angleToTurnInDegrees() {
		if (directions.isEmpty()) {
			return 0;
		}
		return Utils.normalRelativeAngleDegrees(desiredDirection() - getHeading());
	}

	// Returns the distance to move
	private double distanceToMove() {
		// If no keys are pressed, we should not move at all
		if (directions.isEmpty()) {
			return 0;
		}
		// If the robot has more than 45 degrees to turn, move only 5 pixel
		if (Math.abs(angleToTurnInDegrees()) > 45) {
			return 5;
		}
		// Otherwise, move at full speed
		return Double.POSITIVE_INFINITY;
	}

	// Return the desired direction depending on the pending move directions.
	// With one arrow key pressed, the move to N, E, S, W.
	// With two keys pressed, the robot also move to NE, NW, SE, SW.
	private double desiredDirection() {
		if (directions.contains(Direction.UP)) {
			if (directions.contains(Direction.RIGHT)) {
				return 45;
			}
			if (directions.contains(Direction.LEFT)) {
				return 315;
			}
			return 0;
		}
		if (directions.contains(Direction.DOWN)) {
			if (directions.contains(Direction.RIGHT)) {
				return 135;
			}
			if (directions.contains(Direction.LEFT)) {
				return 225;
			}
			return 180;
		}
		if (directions.contains(Direction.RIGHT)) {
			return 90;
		}
		if (directions.contains(Direction.LEFT)) {
			return 270;
		}
		return 0;
	}
}
