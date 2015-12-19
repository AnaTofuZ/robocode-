/**
*Copyryghtの説明。このデフォルトで設定されている
*/

package sample;

// Interactibe.javaが収録されているpackegeの指定。今回はsample

import robocode.AdvancedRovot;

//robocode.AdvancedRovotからメソット等をimportする

import static robocode.util.Utils.normalAbsoluteAngle;
import static robocode.util.Utils.normalRelativeAngle;

//staticとしてrobocodeクラス内のUtils.メソッドをimport

import java.awt.*;
import java.event.keyEvent;
import static java.awt.event.keyEvent.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


/**
* Interactive はFlemming N. Larsen.によって作られたsample robot
*<p/>
*このrobotはコントロールをKeyboardとマウスのみで行う
*<p/>
*Keys:

- W または ar
 
