/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication9;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class FloatingText {
    double x, y;
    String text;
    float r, g, b;
    double life = 1.0;
    double yOffset = 0;
    private static GLUT glut = null;
    
    // Fix the constructor - match what Game.java is calling
    public FloatingText(double x, double y, String text, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    // Keep the old constructor for compatibility if needed
    public FloatingText(double x, double y, String text, int r, int g, int b) {
        this(x, y, text, r / 255.0f, g / 255.0f, b / 255.0f);
    }
    
    public void update() {
        yOffset += 1.2;
        life -= 0.02;
    }
    
    public void render(GL2 gl) {
        if (life <= 0) return;
        if (glut == null) initGLUT();
        
        gl.glColor4f(r, g, b, (float)life);
        gl.glRasterPos2d(x, y - yOffset);
        for (char c : text.toCharArray()) {
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }
    
    public static void initGLUT() {
        if (glut == null) {
            glut = new GLUT();
        }
    }
    
    public boolean isDead() {
        return life <= 0;
    }
}
