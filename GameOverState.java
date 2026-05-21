// GameOverState.java
package javaapplication9;

import com.jogamp.opengl.GL2;
import java.awt.event.KeyEvent;

public class GameOverState implements GameState {

    @Override
    public void update(Game game) {}
    
    @Override
    public void render(GL2 gl, Game game) {
        game.renderGameOver(gl);
    }
    
   // GameOverState.java - تعديل
@Override
public void keyPressed(KeyEvent e, Game game) {
    if (e.getKeyCode() == KeyEvent.VK_R) {
        game.initGame();
        game.getStateManager().setState(new PlayState(game.getGameWidth(), game.getGameHeight()));
    }
}
    @Override
    public void keyReleased(KeyEvent e, Game game) {}
}