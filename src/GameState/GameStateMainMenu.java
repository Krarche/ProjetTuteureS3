/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GameState;

import GUI.GUIHelp;
import GUI.GUILabel;
import GUI.GUIMenuButton;
import GUI.GUITexture;
import Tools.Input;
import Tests.Main;
import static Graphic.GraphicMain.window;
import Tools.Lang;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_TRUE;

public class GameStateMainMenu extends GameState {
	
	@Override
	public void onEnter() {
		glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_NORMAL);
	}
	public GameStateMainMenu() {
		super();
		
		GUITexture t= new GUITexture("logo");
		t.setPos(100,100);
		t.setSize(75,75);
		//m_GUI.add(t);
		
		GUILabel l=new GUILabel(GUILabel.ALIGN_CENTER);
		l.setLabelText(Lang.getString("HUD_Label_MainMenu"));
		l.setPos(0.5f,0.8f);
		l.setSize(0,0.1f);
		l.setLabelSize(0.15f);
		m_GUI.add(l);
		
		GUIHelp h;
		
		h=new GUIHelp();
		h.setLabelText(Lang.getString("HUD_Help_Play"));
		h.setLabelSize(0.1f);
		h.setSize(0.15f,0.05f);
		h.setPos(0,200f);
		h.setModelColor(0,0.5f,1,0.5f,1);
		
		GUIMenuButton m=new GUIMenuButton(Main.STATE_LEVEL);
		m.setLabelText(Lang.getString("HUD_Button_Play"));
		m.setPos(0.5f,0.6f);
		m.setSize(0.15f,0.05f);
		m.setLabelSize(0.1f);
		m.setHelp(h);
		m_GUI.add(m);
		
		h=new GUIHelp();
		h.setLabelText(Lang.getString("HUD_Help_Settings"));
		h.setLabelSize(0.1f);
		h.setSize(0.15f,0.05f);
		h.setPos(0,200);
		h.setModelColor(0,0.5f,1,0.5f,1);
		
		m=new GUIMenuButton(Main.STATE_SETTING_MENU);
		m.setLabelText(Lang.getString("HUD_Button_Settings"));
		m.setPos(0.5f,0.4f);
		m.setSize(0.15f,0.05f);
		m.setLabelSize(0.1f);
		m.setHelp(h);
		m_GUI.add(m);
		
		h=new GUIHelp();
		h.setLabelText(Lang.getString("HUD_Help_Quit"));
		h.setLabelSize(0.1f);
		h.setSize(0.15f,0.05f);
		h.setPos(0,200);
		h.setModelColor(0,0.5f,1,0.5f,1);
		
		m=new GUIMenuButton(-1);
		m.setLabelText(Lang.getString("HUD_Button_Quit"));
		m.setPos(0.5f,0.2f);
		m.setSize(0.15f,0.05f);
		m.setLabelSize(0.1f);
		m.setHelp(h);
		m_GUI.add(m);
	}
	@Override
	public void inputHandler() {
		super.inputHandler();
		if(Input.isPushed(GLFW_KEY_1))
			Main.changeGameState(Main.STATE_LEVEL);
		if(Input.isPushed(GLFW_KEY_2))
			Main.changeGameState(Main.STATE_SETTING_MENU);
		if(Input.isPushed(GLFW_KEY_3))
			glfwSetWindowShouldClose(window, GL_TRUE);
	}
}
