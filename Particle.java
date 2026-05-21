package javaapplication9;

import com.jogamp.opengl.GL2;

public class Particle {
    double x, y;
    double vx, vy;
    float r, g, b;
    double life = 1.0;
    double decay = 0.02;
    
    public Particle(double x, double y, double vx, double vy, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public void update() {
        x += vx;
        y += vy;
        vy += 0.2; // gravity
        life -= decay;
    }
    
    public void render(GL2 gl) {
        gl.glColor4f(r, g, b, (float)life);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x - 2, y - 2);
        gl.glVertex2d(x + 2, y - 2);
        gl.glVertex2d(x + 2, y + 2);
        gl.glVertex2d(x - 2, y + 2);
        gl.glEnd();
    }
    
    public boolean isDead() {
        return life <= 0;
    }
}
