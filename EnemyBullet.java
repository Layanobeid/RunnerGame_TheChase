/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication9;

import com.jogamp.opengl.GL2;

public class EnemyBullet {
    double x, y;
    double vx, vy;
    double size = 6;
    boolean active = true;
    
    public EnemyBullet(double x, double y, double targetX, double targetY) {
    this.x = x;
    this.y = y;
    
    double dx = targetX - x;
    double dy = targetY - y;
    double length = Math.sqrt(dx * dx + dy * dy);
    
    if (length > 0) {
        this.vx = (dx / length) * 6;
        this.vy = (dy / length) * 6;
    } else {
        this.vx = -5;
        this.vy = 0;
    }
}
    
    public void update() {
        x += vx;
        y += vy;
        
        if (x < -50 || x > 850 || y < -50 || y > 650) {
            active = false;
        }
    }
    
    public void render(GL2 gl) {
        // تأثير توهج أحمر
        gl.glColor4f(1, 0, 0, 0.7f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x - 3, y - 3);
        gl.glVertex2d(x + 3, y - 3);
        gl.glVertex2d(x + 3, y + 3);
        gl.glVertex2d(x - 3, y + 3);
        gl.glEnd();
        
        // الرصاصة الأساسية
        gl.glColor3f(1, 0.3f, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x - 2, y - 2);
        gl.glVertex2d(x + 2, y - 2);
        gl.glVertex2d(x + 2, y + 2);
        gl.glVertex2d(x - 2, y + 2);
        gl.glEnd();
    }
}
