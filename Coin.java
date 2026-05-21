package javaapplication9;

import com.jogamp.opengl.GL2;

public class Coin {
    double x, y;
    double size = 20;
    double speed = 5;
    double rotation = 0;
    
    public Coin(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void update() {
        x -= speed;
        rotation += 0.1;
    }
    
   public void render(GL2 gl) {

    double cx = x + size / 2;
    double cy = y + size / 2;

    double radius = size / 2;

    // ===== rotation effect =====
    rotation += 0.15;
    double spin = Math.sin(rotation) * 0.15;

    // ===== OUTER GLOW (خفيف) =====
    gl.glColor4f(1.0f, 0.85f, 0.0f, 0.25f);

    gl.glBegin(GL2.GL_TRIANGLE_FAN);
    for (int a = 0; a <= 360; a += 20) {
        double rad = Math.toRadians(a);
        gl.glVertex2d(
            cx + Math.cos(rad) * (radius + 4),
            cy + Math.sin(rad) * (radius + 4)
        );
    }
    gl.glEnd();

    // ===== MAIN COIN =====
    gl.glColor3f(1.0f, 0.8f, 0.0f);

    gl.glBegin(GL2.GL_TRIANGLE_FAN);
    for (int a = 0; a <= 360; a += 15) {
        double rad = Math.toRadians(a);
        gl.glVertex2d(
            cx + Math.cos(rad) * (radius + spin),
            cy + Math.sin(rad) * (radius + spin)
        );
    }
    gl.glEnd();

    // ===== INNER CIRCLE =====
    gl.glColor3f(1.0f, 0.65f, 0.0f);

    gl.glBegin(GL2.GL_TRIANGLE_FAN);
    for (int a = 0; a <= 360; a += 20) {
        double rad = Math.toRadians(a);
        gl.glVertex2d(
            cx + Math.cos(rad) * (radius * 0.5),
            cy + Math.sin(rad) * (radius * 0.5)
        );
    }
    gl.glEnd();

    // ===== SIMPLE SYMBOL (clean + visible) =====
    gl.glColor3f(1, 1, 1);

    gl.glBegin(GL2.GL_LINES);

    // vertical line $
    gl.glVertex2d(cx, cy - 6);
    gl.glVertex2d(cx, cy + 6);

    // top bar
    gl.glVertex2d(cx - 4, cy - 3);
    gl.glVertex2d(cx + 4, cy - 3);

    // bottom bar
    gl.glVertex2d(cx - 4, cy + 3);
    gl.glVertex2d(cx + 4, cy + 3);

    gl.glEnd();
}
    public boolean isOffScreen() {
        return x + size < 0;
    }

   
}