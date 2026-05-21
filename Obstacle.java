package javaapplication9;

import com.jogamp.opengl.GL2;

public class Obstacle {
    double x;
    double y;
    double width = 30;
    double height = 50;
    double speed = 5;
    boolean hasHit = false;      // هل ضرب اللاعب قبل هيك؟
    long lastHitTime = 0;        // آخر مرة ضرب فيها
    
    public Obstacle(double startX, double startY, double width) {
        this.x = startX;
        this.y = startY;
        this.width = width;
    }
    
    public void update() {
        x -= speed;
    }
    
    public void render(GL2 gl) {
        // إذا ضرب من قبل، نغير لونه شوي
        if (hasHit) {
            gl.glColor3f(0.6f, 0.3f, 0.2f); // لون أفتح للعائق المصاب
        } else {
            gl.glColor3f(0.4f, 0.24f, 0.16f);
        }
        
        // Main obstacle body
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x, y);
        gl.glVertex2d(x + width, y);
        gl.glVertex2d(x + width, y + height);
        gl.glVertex2d(x, y + height);
        gl.glEnd();
        
        // Texture/details
        gl.glColor3f(0.3f, 0.18f, 0.12f);
        for (int i = 0; i < height; i += 10) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2d(x + 5, y + i);
            gl.glVertex2d(x + width - 5, y + i);
            gl.glVertex2d(x + width - 5, y + i + 3);
            gl.glVertex2d(x + 5, y + i + 3);
            gl.glEnd();
        }
        
        // Spikes on top
        gl.glColor3f(0.5f, 0.3f, 0.2f);
        for (int i = 0; i < width; i += 10) {
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex2d(x + i, y);
            gl.glVertex2d(x + i + 5, y - 8);
            gl.glVertex2d(x + i + 10, y);
            gl.glEnd();
        }
        
        // Shadow
        gl.glColor4f(0, 0, 0, 0.3f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x + 5, y + height - 5);
        gl.glVertex2d(x + width - 5, y + height - 5);
        gl.glVertex2d(x + width - 5, y + height);
        gl.glVertex2d(x + 5, y + height);
        gl.glEnd();
        
        // إذا ضرب، نظهر علامة X أو تأثير
        if (hasHit && System.currentTimeMillis() - lastHitTime < 300) {
            gl.glColor3f(1, 0, 0);
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2d(x + 5, y + 5);
            gl.glVertex2d(x + width - 5, y + height - 5);
            gl.glVertex2d(x + width - 5, y + 5);
            gl.glVertex2d(x + 5, y + height - 5);
            gl.glEnd();
        }
    }
    
    public void reset() {
        hasHit = false;
    }
    
    public boolean isOffScreen() {
        return x + width < 0;
    }
}