/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication9;

import com.jogamp.opengl.GL2;
import java.awt.event.KeyEvent;

public interface GameState {
    void update(Game game);
    void render(GL2 gl, Game game);
    void keyPressed(KeyEvent e, Game game);
    void keyReleased(KeyEvent e, Game game);
}