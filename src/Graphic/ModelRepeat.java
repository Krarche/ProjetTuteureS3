/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Graphic;

import Maths.Vector2f;
import Maths.Vector4f;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;

public class ModelRepeat extends Model {
	public ModelRepeat() {
		super();
	}
	
	@Override
    public void draw(Vector2f pos,Vector2f size, Vector4f colors) {
        startRender(pos,size);
        render(colors,size);
        endRender();
    }
    public void render(Vector4f colors,Vector2f size) {
        glBegin(GL_TRIANGLES);
        float[] vertice=m_vertice.array();
        float[] texture=m_textures.array();
        sendColors(colors);
        for(int i=0;i<m_verticeCount;i++) {
			if(m_texture!=null) // send texture
				sendTextures(i,texture,size);
            sendVertice(i,vertice);
        }
        glEnd();
    }
    public void sendTextures(int offset,float[]data,Vector2f size) {
        glTexCoord2f(Math.round(data[offset*2]*size.x*2)*GraphicMain.DIRECTION,Math.round(data[offset*2+1]*size.y*2));
    }
}
