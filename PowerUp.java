/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication9;

/**
 *
 * @author LAYAN
 */
import com.jogamp.opengl.GL2;

public class PowerUp {
    double x, y;
    double size = 25;
    double speed = 5;
    int type; // 0 = star, 1 = heart
    float hue = 0;
    
    public PowerUp(double x, double y) {
        this.x = x;
        this.y = y;
        this.type = (int)(Math.random() * 2);
    }
    
    public void update() {
        x -= speed;
        hue += 0.02f;
        if (hue > 1) hue = 0;
    }
    
    public void render(GL2 gl) {
        // Rainbow effect using HSV
        float r = (float)Math.sin(hue * Math.PI * 2);
        float g = (float)Math.sin((hue + 0.33f) * Math.PI * 2);
        float b = (float)Math.sin((hue + 0.67f) * Math.PI * 2);
        
        gl.glColor3f(r, g, b);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x, y);
        gl.glVertex2d(x + size, y);
        gl.glVertex2d(x + size, y + size);
        gl.glVertex2d(x, y + size);
        gl.glEnd();
        
        // Inner symbol
        gl.glColor3f(1, 1, 1);
        //heart
   gl.glBegin(GL2.GL_POLYGON);

double scale = size / 35.0;

for (double t = 0; t < Math.PI * 2; t += 0.1) {

    double xh = 16 * Math.pow(Math.sin(t), 3);

    double yh =
            13 * Math.cos(t)
          - 5 * Math.cos(2 * t)
          - 2 * Math.cos(3 * t)
          - Math.cos(4 * t);

    gl.glVertex2d(
        x + size / 2 + xh * scale,
        y + size / 2 - yh * scale
    );
}

gl.glEnd();
    }
    
    
    public boolean isOffScreen() {
        return x + size < 0;
    }
}
