/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication9;

import com.jogamp.opengl.GL2;
import java.util.Random;

public class Boss {
    double x, y;
    double width = 80;
    double height = 90;
    int health = 5;
    int maxHealth = 5;
    double speed = 2.5;
    boolean active = true;
    double bobOffset = 0;
    int attackCooldown = 0;
    Random random = new Random();
    int groundY=480;
    public Boss(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void update(double playerX, double playerY) {
        x -= speed;
        bobOffset += 0.05;
        
        y = groundY + Math.sin(bobOffset) * 8;
        
        if (attackCooldown > 0) {
            attackCooldown--;
        }
    }
    
    public void render(GL2 gl) {
        // ظل
        gl.glColor4f(0, 0, 0, 0.4f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x + 10, y + height - 10);
        gl.glVertex2d(x + width - 10, y + height - 10);
        gl.glVertex2d(x + width - 10, y + height);
        gl.glVertex2d(x + 10, y + height);
        gl.glEnd();
        
        // الجسم الرئيسي (أحمر غامق مع توهج)
        float intensity = 0.6f + (float)Math.sin(System.currentTimeMillis() / 200.0) * 0.2f;
        gl.glColor3f(0.8f * intensity, 0.2f, 0.2f);
        drawRoundedRect(gl, x, y, width, height, 15);
        
        // عيون حمراء متوهجة
        double eyeY = y + 25;
        double leftEye = x + 20;
        double rightEye = x + 55;
        
        // توهج العيون
        gl.glColor4f(1, 0, 0, 0.5f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(leftEye - 5, eyeY - 5);
        gl.glVertex2d(leftEye + 15, eyeY - 5);
        gl.glVertex2d(leftEye + 15, eyeY + 15);
        gl.glVertex2d(leftEye - 5, eyeY + 15);
        
        gl.glVertex2d(rightEye - 5, eyeY - 5);
        gl.glVertex2d(rightEye + 15, eyeY - 5);
        gl.glVertex2d(rightEye + 15, eyeY + 10);
        gl.glVertex2d(rightEye - 5, eyeY + 10);
        gl.glEnd();
        
        // بؤبؤ العين
        gl.glColor3f(1, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(leftEye + 2, eyeY + 2);
        gl.glVertex2d(leftEye + 10, eyeY + 2);
        gl.glVertex2d(leftEye + 10, eyeY + 10);
        gl.glVertex2d(leftEye + 2, eyeY + 10);
        
        gl.glVertex2d(rightEye + 2, eyeY + 2);
        gl.glVertex2d(rightEye + 10, eyeY + 2);
        gl.glVertex2d(rightEye + 10, eyeY + 8);
        gl.glVertex2d(rightEye + 2, eyeY + 8);
        gl.glEnd();
        
        // فم كبير مع أسنان
        gl.glColor3f(0.3f, 0.1f, 0.1f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x + 25, y + 55);
        gl.glVertex2d(x + 55, y + 55);
        gl.glVertex2d(x + 55, y + 70);
        gl.glVertex2d(x + 25, y + 70);
        gl.glEnd();
        
        // أسنان
        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x + 30, y + 55);
        gl.glVertex2d(x + 37, y + 55);
        gl.glVertex2d(x + 37, y + 62);
        gl.glVertex2d(x + 30, y + 62);
        
        gl.glVertex2d(x + 43, y + 55);
        gl.glVertex2d(x + 50, y + 55);
        gl.glVertex2d(x + 50, y + 62);
        gl.glVertex2d(x + 43, y + 62);
        gl.glEnd();
        
        // شريط الصحة
        double healthPercent = (double)health / maxHealth;
        gl.glColor3f(0.3f, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x, y - 15);
        gl.glVertex2d(x + width, y - 15);
        gl.glVertex2d(x + width, y - 8);
        gl.glVertex2d(x, y - 8);
        gl.glEnd();
        
        gl.glColor3f(1, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x, y - 15);
        gl.glVertex2d(x + width * healthPercent, y - 15);
        gl.glVertex2d(x + width * healthPercent, y - 8);
        gl.glVertex2d(x, y - 8);
        gl.glEnd();
        
        // قرون/أبواق
        gl.glColor3f(0.5f, 0.2f, 0.1f);
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glVertex2d(x + 15, y);
        gl.glVertex2d(x + 25, y - 20);
        gl.glVertex2d(x + 35, y);
        
        gl.glVertex2d(x + 45, y);
        gl.glVertex2d(x + 55, y - 20);
        gl.glVertex2d(x + 65, y);
        gl.glEnd();
    }
    
    private void drawRoundedRect(GL2 gl, double x, double y, double w, double h, double radius) {
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i <= 360; i += 10) {
            double angle = Math.toRadians(i);
            double dx = Math.cos(angle) * radius;
            double dy = Math.sin(angle) * radius;
            
            if (angle < Math.PI/2) {
                gl.glVertex2d(x + w - radius + dx, y + dy);
            } else if (angle < Math.PI) {
                gl.glVertex2d(x + w - radius + dx, y + h - radius + dy);
            } else if (angle < 3*Math.PI/2) {
                gl.glVertex2d(x + radius + dx, y + h - radius + dy);
            } else {
                gl.glVertex2d(x + radius + dx, y + radius + dy);
            }
        }
        gl.glEnd();
    }
    
    public boolean isOffScreen() {
        return x + width < 0;
    }
    
    public void takeDamage() {
        health--;
        if (health <= 0) {
            active = false;
        }
    }
}
