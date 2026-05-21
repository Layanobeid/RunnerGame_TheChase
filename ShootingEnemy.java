/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication9;

import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.Iterator;

public class ShootingEnemy {
    double x, y;
    double width = 35;
    double height = 45;
    double speed = 2.8;
    int health = 2;
    int shootCooldown = 0;
    double bobOffset = 0;
    boolean active = true;
    
    ArrayList<EnemyBullet> bullets;
    int originalY=480;
    public ShootingEnemy(double x, double y, ArrayList<EnemyBullet> bullets) {
        this.x = x;
        this.y = y;
        this.bullets = bullets;
    }
    
    public void update(double playerX, double playerY) {
    // يتتبع اللاعب
    if (x + width < playerX) {
        x += speed * 1.3;
    } else if (x > playerX + 80) {
        x -= speed * 0.3;
    }
    
    bobOffset += 0.08;
    // العدو يبقى على الأرض
y = originalY + Math.sin(bobOffset) * 3; 
    
    if (shootCooldown <= 0 && Math.abs(x - playerX) < 400) {
        EnemyBullet bullet = new EnemyBullet(x + width, y + height/2, playerX, playerY - 20);
        bullets.add(bullet);
        shootCooldown = 60;
    } else {
        shootCooldown--;
    }
}
    
    public void render(GL2 gl) {
        // جسم العدو - أخضر غامق
        gl.glColor3f(0.2f, 0.5f, 0.2f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x, y);
        gl.glVertex2d(x + width, y);
        gl.glVertex2d(x + width, y + height);
        gl.glVertex2d(x, y + height);
        gl.glEnd();
        
        // خوذة عسكرية
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x + 5, y);
        gl.glVertex2d(x + width - 5, y);
        gl.glVertex2d(x + width - 5, y + 15);
        gl.glVertex2d(x + 5, y + 15);
        gl.glEnd();
        
        // عيون
        gl.glColor3f(1, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x + 8, y + 20);
        gl.glVertex2d(x + 15, y + 20);
        gl.glVertex2d(x + 15, y + 27);
        gl.glVertex2d(x + 8, y + 27);
        
        gl.glVertex2d(x + width - 15, y + 20);
        gl.glVertex2d(x + width - 8, y + 20);
        gl.glVertex2d(x + width - 8, y + 27);
        gl.glVertex2d(x + width - 15, y + 27);
        gl.glEnd();
        
        // بندقية
        gl.glColor3f(0.2f, 0.2f, 0.2f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x + width - 10, y + height - 15);
        gl.glVertex2d(x + width + 15, y + height - 12);
        gl.glVertex2d(x + width + 15, y + height - 8);
        gl.glVertex2d(x + width - 10, y + height - 10);
        gl.glEnd();
        
        // شريط الصحة
        double healthPercent = (double)health / 2;
        gl.glColor3f(0.5f, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x + 5, y - 8);
        gl.glVertex2d(x + width - 5, y - 8);
        gl.glVertex2d(x + width - 5, y - 3);
        gl.glVertex2d(x + 5, y - 3);
        gl.glEnd();
        
        gl.glColor3f(0, 1, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x + 5, y - 8);
        gl.glVertex2d(x + 5 + (width - 10) * healthPercent, y - 8);
        gl.glVertex2d(x + 5 + (width - 10) * healthPercent, y - 3);
        gl.glVertex2d(x + 5, y - 3);
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
