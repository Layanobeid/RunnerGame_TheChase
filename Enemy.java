package javaapplication9;

import com.jogamp.opengl.GL2;

public class Enemy {
    double x;
    double y;
    double speed = 2.5;        // سرعة الحركة
    double chaseSpeed = 2.0;   // سرعة الملاحقة
    int frame = 0;
    long lastFrameTime = 0;
      boolean isDead = false;
    double deathTimer = 0;
    
    int enemyType;      // 0 = عادي, 1 = سريع, 2 = كبير
    double width = 35;
    double height = 45;
    double originalY;
    float bobOffset = 0;
    boolean movingUp = true;
    
   public Enemy(double x, double y) {
    this.x = x;
    this.y = y;
    this.originalY = y;
    this.enemyType = (int)(Math.random() * 3);
    
    if (enemyType == 1) { // سريع
        speed = 5;        // زيادة من 4
        chaseSpeed = 4.5; // زيادة من 3.5
        width = 30;
        height = 40;
    } else if (enemyType == 2) { // كبير
        speed = 2.5;      // زيادة من 1.8
        chaseSpeed = 2.0; // زيادة من 1.2
        width = 45;
        height = 55;
    } else { // عادي
        speed = 3.5;      // زيادة من 2.5
        chaseSpeed = 3.0; // زيادة من 2.0
    }
}
    
// Enemy.java - سلوك اندفاعي
public void update(double playerX) {
    if (isDead) {
        deathTimer--;
        return;
    }
    
    bobOffset += 0.08;
    y = originalY + Math.sin(bobOffset) * 3;
    
    double enemyRight = x + width;
    double distance = playerX - enemyRight;
    
    // العدو خلف اللاعب أو أمامه؟
    if (enemyRight < playerX) {
        // العدو خلف اللاعب - يندفع بسرعة كبيرة
        x += speed * 2;
    } 
    else if (enemyRight > playerX + 50) {
        // العدو أمام اللاعب - يتحرك بسرعة متوسطة
        x -= speed;
    }
    else {
        // العدو قريب جداً - يبطئ قليلاً للسماح بالتصادم
        x -= speed * 0.5;
    }
    
    // زيادة السرعة الكلية للأعداء (اجعل speed أكبر)
    // في الـ constructor، زد السرعة:
}
     public void kill() {
        isDead = true;
        deathTimer = 30; // يختفي بعد 30 إطار
    }
    // في Enemy.java - تعديل دالة render() لتكون أكثر وضوحاً

public void render(GL2 gl) {
    if (isDead) {
        gl.glColor4f(1, 1, 1, 0.5f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x, y);
        gl.glVertex2d(x + width, y);
        gl.glVertex2d(x + width, y + height);
        gl.glVertex2d(x, y + height);
        gl.glEnd();
        return;
    }
    
    // لون حسب النوع
    float r, g, b;
    if (enemyType == 0) {
        r = 0.9f; g = 0.2f; b = 0.2f;  // أحمر ساطع
    } else if (enemyType == 1) {
        r = 0.95f; g = 0.5f; b = 0.1f; // برتقالي
    } else {
        r = 0.6f; g = 0.1f; b = 0.1f; // أحمر غامق
    }
    
    // جسم العدو مع حواف داكنة
    gl.glColor3f(r, g, b);
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(x, y);
    gl.glVertex2d(x + width, y);
    gl.glVertex2d(x + width, y + height);
    gl.glVertex2d(x, y + height);
    gl.glEnd();
    
    // حواف (إطار) لتحديد الشكل
    gl.glColor3f(r * 0.6f, g * 0.6f, b * 0.6f);
    gl.glBegin(GL2.GL_LINE_LOOP);
    gl.glVertex2d(x, y);
    gl.glVertex2d(x + width, y);
    gl.glVertex2d(x + width, y + height);
    gl.glVertex2d(x, y + height);
    gl.glEnd();
    
    // عيون سوداء كبيرة
    gl.glColor3f(0, 0, 0);
    double eyeW = 8;
    double eyeH = 10;
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(x + width * 0.25 - eyeW/2, y + height * 0.3);
    gl.glVertex2d(x + width * 0.25 + eyeW/2, y + height * 0.3);
    gl.glVertex2d(x + width * 0.25 + eyeW/2, y + height * 0.3 + eyeH);
    gl.glVertex2d(x + width * 0.25 - eyeW/2, y + height * 0.3 + eyeH);
    
    gl.glVertex2d(x + width * 0.75 - eyeW/2, y + height * 0.3);
    gl.glVertex2d(x + width * 0.75 + eyeW/2, y + height * 0.3);
    gl.glVertex2d(x + width * 0.75 + eyeW/2, y + height * 0.3 + eyeH);
    gl.glVertex2d(x + width * 0.75 - eyeW/2, y + height * 0.3 + eyeH);
    gl.glEnd();
    
    // بؤبؤ أحمر
    gl.glColor3f(1, 0, 0);
    double pupil = 3;
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(x + width * 0.25 - pupil/2, y + height * 0.3 + 3);
    gl.glVertex2d(x + width * 0.25 + pupil/2, y + height * 0.3 + 3);
    gl.glVertex2d(x + width * 0.25 + pupil/2, y + height * 0.3 + 3 + pupil);
    gl.glVertex2d(x + width * 0.25 - pupil/2, y + height * 0.3 + 3 + pupil);
    
    gl.glVertex2d(x + width * 0.75 - pupil/2, y + height * 0.3 + 3);
    gl.glVertex2d(x + width * 0.75 + pupil/2, y + height * 0.3 + 3);
    gl.glVertex2d(x + width * 0.75 + pupil/2, y + height * 0.3 + 3 + pupil);
    gl.glVertex2d(x + width * 0.75 - pupil/2, y + height * 0.3 + 3 + pupil);
    gl.glEnd();
    
    // فم شرير
    gl.glColor3f(0.3f, 0.1f, 0.1f);
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(x + width * 0.3, y + height * 0.7);
    gl.glVertex2d(x + width * 0.7, y + height * 0.7);
    gl.glVertex2d(x + width * 0.7, y + height * 0.85);
    gl.glVertex2d(x + width * 0.3, y + height * 0.85);
    gl.glEnd();
    
    // أسنان
    gl.glColor3f(1, 1, 1);
    gl.glBegin(GL2.GL_TRIANGLES);
    gl.glVertex2d(x + width * 0.35, y + height * 0.7);
    gl.glVertex2d(x + width * 0.4, y + height * 0.7);
    gl.glVertex2d(x + width * 0.375, y + height * 0.78);
    
    gl.glVertex2d(x + width * 0.6, y + height * 0.7);
    gl.glVertex2d(x + width * 0.65, y + height * 0.7);
    gl.glVertex2d(x + width * 0.625, y + height * 0.78);
    gl.glEnd();
}
    
    private void drawRoundedRect(GL2 gl, double x, double y, double w, double h, double radius) {
        // رسم مستطيل مدور باستخدام مثلثات
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
    
    private void drawMouth(GL2 gl, double cx, double cy, double w, double h) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(cx - w/2, cy);
        gl.glVertex2d(cx + w/2, cy);
        gl.glVertex2d(cx + w/2, cy + h);
        gl.glVertex2d(cx - w/2, cy + h);
        gl.glEnd();
    }
    
    private void drawSmile(GL2 gl, double cx, double cy, double w, double h) {
        gl.glBegin(GL2.GL_POLYGON);
        for (int a = 180; a <= 360; a += 10) {
            double rad = Math.toRadians(a);
            gl.glVertex2d(cx + Math.cos(rad) * w/2, cy + Math.sin(rad) * h/2);
        }
        gl.glEnd();
    }
    
    private void drawBigMouth(GL2 gl, double cx, double cy, double w, double h) {
        gl.glColor3f(0.5f, 0.1f, 0.1f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(cx - w/2, cy);
        gl.glVertex2d(cx + w/2, cy);
        gl.glVertex2d(cx + w/2, cy + h);
        gl.glVertex2d(cx - w/2, cy + h);
        gl.glEnd();
        
        // أسنان
        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(cx - 5, cy);
        gl.glVertex2d(cx - 1, cy);
        gl.glVertex2d(cx - 1, cy + 5);
        gl.glVertex2d(cx - 5, cy + 5);
        
        gl.glVertex2d(cx + 1, cy);
        gl.glVertex2d(cx + 5, cy);
        gl.glVertex2d(cx + 5, cy + 5);
        gl.glVertex2d(cx + 1, cy + 5);
        gl.glEnd();
    }
    
    public boolean isOffScreen() {
        return x + width < 0;
    }
}