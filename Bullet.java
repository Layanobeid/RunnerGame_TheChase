package javaapplication9;

import com.jogamp.opengl.GL2;

public class Bullet {
    double x, y;
    double speed = 12;
    double size = 8;
    boolean active = true;
    
    public Bullet(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void update() {
        x += speed;
        if (x > 800) active = false;
    }
    
    public void render(GL2 gl) {
        // تأثير توهج للرصاصة
        gl.glColor4f(1, 0.8f, 0, 0.5f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x - 2, y - size/2 - 2);
        gl.glVertex2d(x + size + 2, y - size/2 - 2);
        gl.glVertex2d(x + size + 2, y + size/2 + 2);
        gl.glVertex2d(x - 2, y + size/2 + 2);
        gl.glEnd();
        
        gl.glColor3f(1, 1, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x, y - size/2);
        gl.glVertex2d(x + size, y - size/2);
        gl.glVertex2d(x + size, y + size/2);
        gl.glVertex2d(x, y + size/2);
        gl.glEnd();
    }
    
public boolean checkHit(Boss boss) {
    return x < boss.x + boss.width &&
           x + size > boss.x &&
           y - size/2 < boss.y + boss.height &&
           y + size/2 > boss.y;
}
}