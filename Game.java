package javaapplication9;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game implements GLEventListener, KeyListener {
    private GLJPanel panel;
    private FPSAnimator animator;
    StateManager stateManager = new StateManager();
    
    private int width = 800;
    int height = 600;
    private int menuSelection = 0;
    private static int globalHighScore = 0;
    private int highScore = 0;
    private double bgOffset = 0;
    
    public Game() {
        FloatingText.initGLUT();
        TextRenderer.initGLUT();
    }
    // في Game.java
public int getGameWidth() {
    return width;
}

public int getGameHeight() {
    return height;
}
    public void start() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        panel = new GLJPanel(caps);
        panel.addGLEventListener(this);
        panel.addKeyListener(this);
        panel.setFocusable(true);
        panel.setSize(width, height);
        
        JFrame frame = new JFrame("Runner Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.add(panel);
        frame.setVisible(true);
        
        animator = new FPSAnimator(panel, 60);
        animator.start();
    }
    
    public void setMenuSelection(int selection) {
        this.menuSelection = selection;
    }
    
    void initGame() {
        Sound.play("gamestart.wav");
        if (globalHighScore < highScore) {
            globalHighScore = highScore;
        }
        highScore = globalHighScore;
        stateManager.setState(new PlayState(width, height));
    }
    
    @Override
    public void init(GLAutoDrawable drawable) {
            if (globalHighScore < highScore) {
        globalHighScore = highScore;
    }
    highScore = globalHighScore;
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.2f, 0.6f, 1.0f, 1.0f);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_LINE_SMOOTH);
        gl.glEnable(GL2.GL_POINT_SMOOTH);
        
        stateManager.setState(new MenuState());
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        
        renderSky(gl);
        renderClouds(gl);
        renderGround(gl);
        
        if (stateManager != null) {
            stateManager.update(this);
            stateManager.render(gl, this);
        }
    }
    
 @Override
public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    this.width = width;
    this.height = height;
    
    GL2 gl = drawable.getGL().getGL2();
    gl.glViewport(0, 0, width, height);
    
    TextRenderer.setWindowHeight(height);
    
    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glLoadIdentity();
    // استخدم (0, width, height, 0) بدلاً من (0, width, 0, height)
gl.glOrtho(0, width, height, 0, -1, 1);  
  // ← هذا هو التغيير المهم
    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glLoadIdentity();
}
    
    @Override
    public void dispose(GLAutoDrawable drawable) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (stateManager != null) {
            stateManager.keyPressed(e, this);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (stateManager != null) {
            stateManager.keyReleased(e, this);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public StateManager getStateManager() {
        return stateManager;
    }
    
  
    public int getHighScore() {
        return highScore;
    }
    
    public void setHighScore(int score) {
        this.highScore = score;
        if (score > globalHighScore) {
            globalHighScore = score;
        }
    }
    
    // دوال الرسم الأساسية
    public void renderSky(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.2f, 0.6f, 1.0f);
        gl.glVertex2d(0, 0);
        gl.glColor3f(0.4f, 0.7f, 1.0f);
        gl.glVertex2d(width, 0);
        gl.glColor3f(0.7f, 0.8f, 1.0f);
        gl.glVertex2d(width, height);
        gl.glColor3f(0.3f, 0.65f, 1.0f);
        gl.glVertex2d(0, height);
        gl.glEnd();
    }
    
    public void renderClouds(GL2 gl) {
        gl.glColor4f(1, 1, 1, 0.7f);
        double cloudX = (bgOffset * 0.3) % (width + 200) - 100;
        bgOffset += 2;
        
        for (int i = 0; i < 3; i++) {
            double x = cloudX + i * 300;
            drawCloud(gl, x, 80);
            drawCloud(gl, x + 150, 50);
        }
    }
    
    private void drawCloud(GL2 gl, double x, double y) {
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        for (int a = 0; a <= 360; a += 30) {
            double rad = Math.toRadians(a);
            gl.glVertex2d(x + Math.cos(rad) * 30, y + Math.sin(rad) * 20);
        }
        gl.glEnd();
    }
public void renderGround(GL2 gl) {
   gl.glColor3f(0.15f, 0.65f, 0.15f);  // أخضر أفتح وأجمل
gl.glBegin(GL2.GL_QUADS);
gl.glVertex2d(0, height - 310);
gl.glVertex2d(width, height - 340);
gl.glVertex2d(width, height - 310);
gl.glVertex2d(0, height - 340);
gl.glEnd();

// خطوط العشب التفصيلية
gl.glColor3f(0.2f, 0.75f, 0.2f);  // أخضر فاتح
for (int i = 0; i < 30; i++) {
    double x = (i * 25 + bgOffset * 2) % (width + 50) - 25;
    gl.glBegin(GL2.GL_LINES);
    gl.glVertex2d(x, height - 320);
    gl.glVertex2d(x + 3, height - 330);
    gl.glEnd();
}
    // === طبقة التراب (الأرض الأساسية - تحت) ===
    gl.glColor3f(0.35f, 0.25f, 0.15f);  // لون ترابي غامق
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(0, height - 285);      // ← بدأنا من 285
    gl.glVertex2d(width, height - 285);
    gl.glVertex2d(width, height - 320);
    gl.glVertex2d(0, height - 320);
    gl.glEnd();
    
   
    
    // === حجارة صغيرة على التراب ===
    gl.glColor3f(0.45f, 0.35f, 0.25f);
    for (int i = 0; i < 20; i++) {
        double x = (i * 40 + bgOffset) % (width + 50) - 25;
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x, height - 295);
        gl.glVertex2d(x + 4, height - 295);
        gl.glVertex2d(x + 4, height - 291);
        gl.glVertex2d(x, height - 291);
        gl.glEnd();
    }
    
    // === نقاط صغيرة (تفاصيل في التراب) ===
    gl.glColor3f(0.55f, 0.45f, 0.35f);
    for (int i = 0; i < 50; i++) {
        double x = (i * 30 + bgOffset * 1.5) % (width + 100) - 50;
        double y = height - 305 - (i % 15);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x, y);
        gl.glVertex2d(x + 2, y);
        gl.glVertex2d(x + 2, y + 2);
        gl.glVertex2d(x, y + 2);
        gl.glEnd();
    }
    
    // === حصى متناثرة ===
    gl.glColor3f(0.5f, 0.45f, 0.4f);
    for (int i = 0; i < 15; i++) {
        double x = (i * 70 + bgOffset) % (width + 100) - 50;
        double y = height - 310 + (i % 8);
        gl.glBegin(GL2.GL_POLYGON);
        for (int a = 0; a <= 360; a += 45) {
            double rad = Math.toRadians(a);
            gl.glVertex2d(x + 3 + Math.cos(rad) * 3, y + 2 + Math.sin(rad) * 2);
        }
        gl.glEnd();
    }
}
// رسم زر جميل
private void drawButton(GL2 gl, double x, double y, double w, double h, boolean hover, float r, float g, float b) {
    // ظل الزر
    gl.glColor4f(0, 0, 0, 0.3f);
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(x + 3, y + 3);
    gl.glVertex2d(x + w + 3, y + 3);
    gl.glVertex2d(x + w + 3, y + h + 3);
    gl.glVertex2d(x + 3, y + h + 3);
    gl.glEnd();
    
    // جسم الزر
    float brightness = hover ? 1.2f : 0.8f;
    gl.glColor3f(r * brightness, g * brightness, b * brightness);
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(x, y);
    gl.glVertex2d(x + w, y);
    gl.glVertex2d(x + w, y + h);
    gl.glVertex2d(x, y + h);
    gl.glEnd();
    
    // إطار الزر
    gl.glColor3f(1, 1, 1);
    gl.glBegin(GL2.GL_LINE_LOOP);
    gl.glVertex2d(x, y);
    gl.glVertex2d(x + w, y);
    gl.glVertex2d(x + w, y + h);
    gl.glVertex2d(x, y + h);
    gl.glEnd();
    
    // تأثير hover
    if (hover) {
        gl.glColor4f(1, 1, 1, 0.2f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(x + 2, y + 2);
        gl.glVertex2d(x + w - 2, y + 2);
        gl.glVertex2d(x + w - 2, y + h - 2);
        gl.glVertex2d(x + 2, y + h - 2);
        gl.glEnd();
    }
}

// رسم نجوم متحركة
private void drawStars(GL2 gl) {
    // نجوم ثابتة
    int[][] stars = {
        {50, 80}, {150, 50}, {250, 120}, {400, 40}, {550, 90}, 
        {650, 60}, {750, 110}, {100, 200}, {700, 180}, {350, 160}
    };
    
    for (int[] star : stars) {
        float twinkle = (float)(Math.sin(System.currentTimeMillis() / 1000.0 + star[0]) * 0.5f + 0.5f);
        gl.glColor4f(1, 1, 1, twinkle * 0.8f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(star[0], star[1]);
        gl.glVertex2d(star[0] + 2, star[1]);
        gl.glVertex2d(star[0] + 2, star[1] + 2);
        gl.glVertex2d(star[0], star[1] + 2);
        gl.glEnd();
    }
}

public void renderMenu(GL2 gl) {
    // خلفية
    gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
    gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    
    // نجوم في الخلفية
    drawStars(gl);
    
    // ===== الترتيب من الأعلى إلى الأسفل (Y تبدأ من 50 فصاعداً) =====
    int currentY = 50;  // نبدأ من أعلى الشاشة
    
    // 1. RUNNER GAME (أكبر عنوان)
    float titleGlow = (float)(Math.sin(System.currentTimeMillis() / 800.0) * 0.2f + 0.8f);
    TextRenderer.drawStringLarge(gl, "RUNNER GAME", width/2 - 100, currentY, titleGlow, 0.9f, 0.4f);
    currentY += 45;
    
    // 2. THE CHASE
    TextRenderer.drawStringLarge(gl, "THE CHASE", width/2 - 65, currentY, 1, 0.7f, 0.2f);
    currentY += 40;
    
    // 3. Endless Runner Game + Java + JOGL
// صحيح - يجب أن يكون:
TextRenderer.drawString(gl, "Endless Runner Game", width/2 - 90, currentY, 0.7f, 0.7f, 0.8f);
    currentY += 22;
    TextRenderer.drawString(gl, "Java + JOGL (OpenGL)", width/2 - 95, currentY);
    currentY += 45;
   
    // ===== الإطار الأبيض للمعلومات =====
    int infoStartY = currentY;
    int boxHeight = 350;
    int boxHalfWidth = 250;
    
    gl.glColor4f(1, 1, 1, 0.3f);
    gl.glBegin(GL2.GL_LINE_LOOP);
    gl.glVertex2d(width/2 - boxHalfWidth, infoStartY);
    gl.glVertex2d(width/2 + boxHalfWidth, infoStartY);
    gl.glVertex2d(width/2 + boxHalfWidth, infoStartY + boxHeight);
    gl.glVertex2d(width/2 - boxHalfWidth, infoStartY + boxHeight);
    gl.glEnd();
    
    // المعلومات داخل الإطار (من الأعلى إلى الأسفل داخل الإطار)
    int innerY = infoStartY + 95;
    
    // Collect Coins
    TextRenderer.drawString(gl, "Collect Coins", width/2 - 190, innerY);
    TextRenderer.drawString(gl, "+10 Points", width/2-10 , innerY);
    innerY += 32;
    
    // Defeat Enemies
    TextRenderer.drawString(gl, "Defeat Enemies", width/2 - 190, innerY);
    TextRenderer.drawString(gl, "+30 to +120 Points", width/2-10 , innerY);
    innerY += 32;
    
    // Power-ups
    TextRenderer.drawString(gl, "Power-ups", width/2 - 190, innerY);
    TextRenderer.drawString(gl, "+50 Points or +1 Life", width/2-10 , innerY);
    innerY += 32;
    
    // Boss Fights
    TextRenderer.drawString(gl, "Boss Fights", width/2 - 190, innerY);
    TextRenderer.drawString(gl, "Defeat Boss & earn +200 Points", width/2-10 , innerY);
    
    currentY = infoStartY + boxHeight + 40;
    
    // ===== الأزرار (START و QUIT) =====
    double buttonY = currentY;
    
    // إطار الأزرار الخارجي
    gl.glColor4f(1, 1, 1, 0.2f);
    gl.glBegin(GL2.GL_LINE_LOOP);
    gl.glVertex2d(width/2 - 90, buttonY - 5);
    gl.glVertex2d(width/2 + 100, buttonY - 5);
    gl.glVertex2d(width/2 + 100, buttonY + 45);
    gl.glVertex2d(width/2 - 90, buttonY + 45);
    gl.glEnd();
    
    // زر START
    if (menuSelection == 0) {
        // ظل
        gl.glColor4f(0, 0.5f, 0, 0.5f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(width/2 - 80, buttonY + 3);
        gl.glVertex2d(width/2 - 10, buttonY + 3);
        gl.glVertex2d(width/2 - 10, buttonY + 38);
        gl.glVertex2d(width/2 - 80, buttonY + 38);
        gl.glEnd();
        // زر
        gl.glColor3f(0.1f, 0.7f, 0.1f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(width/2 - 80, buttonY);
        gl.glVertex2d(width/2 - 10, buttonY);
        gl.glVertex2d(width/2 - 10, buttonY + 35);
        gl.glVertex2d(width/2 - 80, buttonY + 35);
        gl.glEnd();
        
        TextRenderer.drawString(gl, "START", width/2 - 75, (int)(buttonY + 23));
        TextRenderer.drawString(gl, "QUIT", width/2 + 30, (int)(buttonY + 23));
    } else {
        // ظل
        gl.glColor4f(0.5f, 0, 0, 0.5f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(width/2 + 20, buttonY + 3);
        gl.glVertex2d(width/2 + 90, buttonY + 3);
        gl.glVertex2d(width/2 + 90, buttonY + 38);
        gl.glVertex2d(width/2 + 20, buttonY + 38);
        gl.glEnd();
        // زر
        gl.glColor3f(0.7f, 0.1f, 0.1f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(width/2 + 20, buttonY);
        gl.glVertex2d(width/2 + 90, buttonY);
        gl.glVertex2d(width/2 + 90, buttonY + 35);
        gl.glVertex2d(width/2 + 20, buttonY + 35);
        gl.glEnd();
        
        TextRenderer.drawString(gl, "START", width/2 - 50, (int)(buttonY + 23));
        TextRenderer.drawString(gl, "QUIT >", width/2 + 40, (int)(buttonY + 23));
    }
    
    // إطارات الأزرار
    gl.glColor3f(0.5f, 0.5f, 0.5f);
    gl.glBegin(GL2.GL_LINE_LOOP);
    gl.glVertex2d(width/2 - 80, buttonY);
    gl.glVertex2d(width/2 - 10, buttonY);
    gl.glVertex2d(width/2 - 10, buttonY + 35);
    gl.glVertex2d(width/2 - 80, buttonY + 35);
    gl.glEnd();
    
    gl.glBegin(GL2.GL_LINE_LOOP);
    gl.glVertex2d(width/2 + 20, buttonY);
    gl.glVertex2d(width/2 + 90, buttonY);
    gl.glVertex2d(width/2 + 90, buttonY + 35);
    gl.glVertex2d(width/2 + 20, buttonY + 35);
    gl.glEnd();
    
    // ===== المعلومات في الأسفل =====
    int bottomY = height - 70;
    TextRenderer.drawString(gl, "Presented by: Layane", width/2 - 85, bottomY);
    TextRenderer.drawString(gl, "Date: 2026", width/2 - 35, bottomY + 25);
}

// In Sound.java - modify play() method to be silent if files missing
public static void play(String filename) {
    try {
        File soundFile = new File("sounds/" + filename);
        if (!soundFile.exists()) {
            // Silent fail - don't print errors
            return;
        }
        // ... rest of code
    } catch (Exception e) {
        // Silent fail
    }
}
public void renderGameOver(GL2 gl) {
    // خلفية سوداء
    gl.glEnable(GL2.GL_BLEND);
    gl.glColor4f(0, 0, 0, 0.85f);
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(0, 0);
    gl.glVertex2d(width, 0);
    gl.glVertex2d(width, height);
    gl.glVertex2d(0, height);
    gl.glEnd();
    
    // GAME OVER - فوق (Y = 120)
    TextRenderer.drawString(gl, "GAME OVER", width/2 - 70, 120);
    
    // إطار النتائج (صندوق أزرق غامق)
    gl.glColor4f(0.1f, 0.1f, 0.3f, 0.7f);
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(width/2 - 130, 170);
    gl.glVertex2d(width/2 + 130, 170);
    gl.glVertex2d(width/2 + 130, 320);
    gl.glVertex2d(width/2 - 130, 320);
    gl.glEnd();
    
    // إطار الصندوق
    gl.glColor3f(0.5f, 0.5f, 0.8f);
    gl.glBegin(GL2.GL_LINE_LOOP);
    gl.glVertex2d(width/2 - 130, 170);
    gl.glVertex2d(width/2 + 130, 170);
    gl.glVertex2d(width/2 + 130, 320);
    gl.glVertex2d(width/2 - 130, 320);
    gl.glEnd();
    
    // FINAL SCORE - النص أولاً (Y = 205)
    TextRenderer.drawString(gl, "FINAL SCORE", width/2 - 55, 205);
    // الرقم تحت النص مباشرة (Y = 230)
    TextRenderer.drawString(gl, String.valueOf(highScore), width/2 - 20, 230);
    
    // خط فاصل بين النتيجتين
    gl.glColor4f(0.5f, 0.5f, 0.8f, 0.5f);
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(width/2 - 110, 255);
    gl.glVertex2d(width/2 + 110, 255);
    gl.glVertex2d(width/2 + 110, 257);
    gl.glVertex2d(width/2 - 110, 257);
    gl.glEnd();
    
    // HIGH SCORE - النص (Y = 275)
    TextRenderer.drawString(gl, "HIGH SCORE", width/2 - 55, 275);
    // الرقم تحت النص (Y = 300)
    TextRenderer.drawString(gl, String.valueOf(globalHighScore), width/2 - 20, 300);
    
    // زر RESTART
    double restartY = height - 100;
    
    // ظل الزر
    gl.glColor4f(0, 0, 0, 0.5f);
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(width/2 - 80, restartY + 3);
    gl.glVertex2d(width/2 + 80, restartY + 3);
    gl.glVertex2d(width/2 + 80, restartY + 43);
    gl.glVertex2d(width/2 - 80, restartY + 43);
    gl.glEnd();
    
    // الزر
    gl.glColor3f(0.2f, 0.5f, 0.2f);
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(width/2 - 80, restartY);
    gl.glVertex2d(width/2 + 80, restartY);
    gl.glVertex2d(width/2 + 80, restartY + 40);
    gl.glVertex2d(width/2 - 80, restartY + 40);
    gl.glEnd();
    
    // إطار الزر
    gl.glColor3f(0, 1, 0);
    gl.glBegin(GL2.GL_LINE_LOOP);
    gl.glVertex2d(width/2 - 80, restartY);
    gl.glVertex2d(width/2 + 80, restartY);
    gl.glVertex2d(width/2 + 80, restartY + 40);
    gl.glVertex2d(width/2 - 80, restartY + 40);
    gl.glEnd();
    
    // نص الزر
    TextRenderer.drawString(gl, "RESTART (R)", width/2 - 55, (int) (restartY + 25));
    
    gl.glDisable(GL2.GL_BLEND);
}
    
    public static void main(String[] args) {
        Sound.play("welcome.wav");
        SwingUtilities.invokeLater(() -> {
            new Game().start();
        });
    }

  public void renderPaused(GL2 gl) {
    gl.glEnable(GL2.GL_BLEND);
    gl.glColor4f(0, 0, 0, 0.7f);
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2d(0, 0);
    gl.glVertex2d(width, 0);
    gl.glVertex2d(width, height);
    gl.glVertex2d(0, height);
    gl.glEnd();
    
    TextRenderer.drawStringLarge(gl, "PAUSED", width/2 - 50, height/2 - 40, 1f, 1f, 0f);
    TextRenderer.drawString(gl, "Press P to Resume", width/2 - 70, height/2 + 20, 0.8f, 0.8f, 0.8f);
    
    gl.glDisable(GL2.GL_BLEND);
}
}