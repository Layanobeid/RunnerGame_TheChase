// TextRenderer.java - الكامل الصحيح

package javaapplication9;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class TextRenderer {
    private static GLUT glut = null;
    private static int windowHeight = 600;
    
    public static void initGLUT() {
        if (glut == null) {
            glut = new GLUT();
        }
    }
    
    public static void setWindowHeight(int height) {
        windowHeight = height;
    }
    
    // الدالة الأساسية - بدون ألوان (أبيض افتراضي)
    public static void drawString(GL2 gl, String str, int x, int y) {
        drawString(gl, str, x, y, 1f, 1f, 1f);
    }
    
    // الدالة الأساسية - مع ألوان
    public static void drawString(GL2 gl, String str, int x, int y, float r, float g, float b) {
        if (glut == null) initGLUT();
        gl.glColor3f(r, g, b);
        gl.glRasterPos2i(x, y);
        for (char c : str.toCharArray()) {
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }
    
    // دالة للرسم بحجم خط أكبر
    public static void drawStringLarge(GL2 gl, String str, int x, int y, float r, float g, float b) {
        if (glut == null) initGLUT();
        gl.glColor3f(r, g, b);
        gl.glRasterPos2i(x, y);
        for (char c : str.toCharArray()) {
            glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_24, c);
        }
    }
    
    // دالة للرسم بحجم خط أكبر - بدون ألوان
    public static void drawStringLarge(GL2 gl, String str, int x, int y) {
        drawStringLarge(gl, str, x, y, 1f, 1f, 1f);
    }
    
    // دالة للرسم بمحاذاة اليمين
    public static void drawStringRight(GL2 gl, String str, int x, int y, float r, float g, float b) {
        int approxWidth = str.length() * 9;
        drawString(gl, str, x - approxWidth, y, r, g, b);
    }
    
    // دالة للرسم بمحاذاة اليمين - بدون ألوان
    public static void drawStringRight(GL2 gl, String str, int x, int y) {
        drawStringRight(gl, str, x, y, 1f, 1f, 1f);
    }
}