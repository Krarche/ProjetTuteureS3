/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import static Graphic.TextRendering.TextRender.getRenderedSize;

public class GUIDialog extends GUIHelp {
	
	protected float m_timer;
	protected int m_letters=0;
	public GUIDialog(String label) {
		super();
		m_modelName="dialog";
		m_timer=0.75f+label.length()*0.05f;
		setLabelText(label);
		setSize(0.75f,0);
		setLabelSize(0.015f);
		m_padding=getRenderedSize(" ",m_labelSize).x;
	}
	@Override
	public String getLabelText() {
		//return m_labelText.substring(0,m_letters);
		return m_labelText;
	}
	public boolean update() {
		if(m_letters<m_labelText.length())
			m_letters++;
		
		m_timer-=Logic.Logic.DELTA_TIME;
		if(m_timer<0)
			return false;
		return true;
	}
	
}
