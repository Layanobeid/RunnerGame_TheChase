/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication9;

import com.jogamp.opengl.GL2;
import java.util.Random;

public class FallingObstacle {
    double x, y;
    double size = 20;
    double speedY = 5;
    boolean active = true;
    Random random = new Random();
    int type; // 0 = حجر, 1 = صخرة كبيرة, 2 = كرة نارية
    
    public FallingObstacle(double x) {
        this.x = x;
        this.y = -random.nextInt(200);
        this.type = random.nextInt(3);
        
        if (type == 1) {
            size = 30;
            speedY = 4;
        } else if (type == 2) {
            size = 18;
            speedY = 7;
        }
    }
    
    public void update() {
        y += speedY;
        
        if (y > 650) {
            active = false;
        }
    }
    
    public void render(GL2 gl) {
        if (type == 0) {
            // حجر رمادي
            gl.glColor3f(0.5f, 0.5f, 0.5f);
            gl.glBegin(GL2.GL_POLYGON);
            for (int a = 0; a <= 360; a += 30) {
                double rad = Math.toRadians(a);
                double rx = x + size/2 + Math.cos(rad) * size/2;
                double ry = y + size/2 + Math.sin(rad) * size/2.5;
                gl.glVertex2d(rx, ry);
            }
            gl.glEnd();
            
            // تفاصيل
            gl.glColor3f(0.3f, 0.3f, 0.3f);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2d(x + 5, y + 10);
            gl.glVertex2d(x + 15, y + 8);
            gl.glVertex2d(x + 12, y + 18);
            gl.glVertex2d(x + 5, y + 15);
            gl.glEnd();
            
        } else if (type == 1) {
            // صخرة كبيرة
            gl.glColor3f(0.4f, 0.3f, 0.2f);
            gl.glBegin(GL2.GL_POLYGON);
            gl.glVertex2d(x, y + size);
            gl.glVertex2d(x + size/2, y);
            gl.glVertex2d(x + size, y + size/2);
            gl.glVertex2d(x + size, y + size);
            gl.glVertex2d(x, y + size);
            gl.glEnd();
            
        } else {
            // كرة نارية
            float r = 0.8f + (float)Math.sin(System.currentTimeMillis() / 50.0) * 0.2f;
            gl.glColor3f(r, 0.3f, 0);
            gl.glBegin(GL2.GL_TRIANGLE_FAN);
            for (int a = 0; a <= 360; a += 20) {
                double rad = Math.toRadians(a);
                gl.glVertex2d(x + size/2 + Math.cos(rad) * size/2,
                             y + size/2 + Math.sin(rad) * size/2);
            }
            gl.glEnd();
            
            // توهج
            gl.glColor4f(1, 0.5f, 0, 0.5f);
            gl.glBegin(GL2.GL_TRIANGLE_FAN);
            for (int a = 0; a <= 360; a += 30) {
                double rad = Math.toRadians(a);
                gl.glVertex2d(x + size/2 + Math.cos(rad) * (size/2 + 3),
                             y + size/2 + Math.sin(rad) * (size/2 + 3));
            }
            gl.glEnd();
        }
        
        // ظل
        gl.glColor4f(0, 0, 0, 0.3f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x, y + size);
        gl.glVertex2d(x + size, y + size);
        gl.glVertex2d(x + size, y + size + 5);
        gl.glVertex2d(x, y + size + 5);
        gl.glEnd();
    }
    
    public boolean checkCollision(double px, double py, double pw, double ph) {
        return px < x + size && px + pw > x && py < y + size && py + ph > y;
    }
}
