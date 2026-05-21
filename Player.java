package javaapplication9;

import com.jogamp.opengl.GL2;

public class Player {
    double x, y;
    double velocityY = 0;
    double gravity = 0.6;
    double jumpPower = -10;
    double maxFallSpeed = 12;

    boolean jumping = false;
    double width = 25;
    double height = 45;
     double groundY = 520; 
    long lastAnimTime = 0;
    int frame = 0;
    double scale = 1.5;
    
    boolean invincible = false;
    long invincibleUntil = 0;
    
    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setInvincible(int milliseconds) {
        invincible = true;
        invincibleUntil = System.currentTimeMillis() + milliseconds;
    }
    
    public void update() {
        if (invincible && System.currentTimeMillis() > invincibleUntil) {
            invincible = false;
        }
        
        velocityY += gravity;
        if (velocityY > maxFallSpeed)
            velocityY = maxFallSpeed;
        
        y += velocityY;
        
        // الهبوط على الأرض
        if (y >= groundY) {
            y = groundY;
            velocityY = 0;
            jumping = false;
        }
        
        // منع اللاعب من الطيران فوق الشاشة
        if (y - height < 0) {
            y = height;
            velocityY = 0;
        }
        
        long now = System.nanoTime();
        if (now - lastAnimTime > 180_000_000) {
            frame = (frame + 1) % 2;
            lastAnimTime = now;
        }
    }
    
    public void jump() {
        if (!jumping) {
            velocityY = jumpPower;
            jumping = true;
        }
    }
    
    public void render(GL2 gl) {
        double w = width * scale;
        double h = height * scale;
        double topY = y - h;
        double bottomY = y;
        double centerX = x + w / 2;
        
        if (invincible && (System.currentTimeMillis() / 100) % 2 == 0) {
            gl.glColor4f(1, 1, 1, 0.5f);
        } else {
            gl.glColor3f(jumping ? 0.25f : 0.0f, 0.45f, 0.85f);
        }
        
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(centerX - 12, topY + 15);
        gl.glVertex2d(centerX + 12, topY + 15);
        gl.glVertex2d(centerX + 12, bottomY - 8);
        gl.glVertex2d(centerX - 12, bottomY - 8);
        gl.glEnd();
        
        gl.glColor3f(1.0f, 0.85f, 0.7f);
        double headSize = 18;
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(centerX - headSize/2, topY);
        gl.glVertex2d(centerX + headSize/2, topY);
        gl.glVertex2d(centerX + headSize/2, topY + headSize);
        gl.glVertex2d(centerX - headSize/2, topY + headSize);
        gl.glEnd();
        
        boolean blink = (System.currentTimeMillis() / 2500) % 2 == 0;
        if (!blink && !invincible) {
            gl.glColor3f(0, 0, 0);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2d(centerX - 6, topY + 5);
            gl.glVertex2d(centerX - 3, topY + 5);
            gl.glVertex2d(centerX - 3, topY + 8);
            gl.glVertex2d(centerX - 6, topY + 8);
            
            gl.glVertex2d(centerX + 3, topY + 5);
            gl.glVertex2d(centerX + 6, topY + 5);
            gl.glVertex2d(centerX + 6, topY + 8);
            gl.glVertex2d(centerX + 3, topY + 8);
            gl.glEnd();
        }
        
        gl.glColor3f(0, 0, 0);
        double legMove = (frame == 0) ? 3 : -3;
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(centerX - 8 + legMove, bottomY - 8);
        gl.glVertex2d(centerX - 3 + legMove, bottomY - 8);
        gl.glVertex2d(centerX - 3 + legMove, bottomY + 8);
        gl.glVertex2d(centerX - 8 + legMove, bottomY + 8);
        
        gl.glVertex2d(centerX + 3 - legMove, bottomY - 8);
        gl.glVertex2d(centerX + 8 - legMove, bottomY - 8);
        gl.glVertex2d(centerX + 8 - legMove, bottomY + 8);
        gl.glVertex2d(centerX + 3 - legMove, bottomY + 8);
        gl.glEnd();
        
        gl.glColor3f(1.0f, 0.78f, 0.4f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(centerX - 15, topY + 20);
        gl.glVertex2d(centerX - 12, topY + 20);
        gl.glVertex2d(centerX - 12, topY + 35);
        gl.glVertex2d(centerX - 15, topY + 35);
        
        gl.glVertex2d(centerX + 12, topY + 20);
        gl.glVertex2d(centerX + 15, topY + 20);
        gl.glVertex2d(centerX + 15, topY + 35);
        gl.glVertex2d(centerX + 12, topY + 35);
        gl.glEnd();
    }
}