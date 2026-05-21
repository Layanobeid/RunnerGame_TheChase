// PauseState.java
package javaapplication9;

import com.jogamp.opengl.GL2;
import java.awt.event.KeyEvent;

public class PauseState implements GameState {
    private PlayState playState;
    
    public PauseState(PlayState playState) {
        this.playState = playState;
    }
    
    @Override
    public void update(Game game) {
        // لا شيء - اللعبة متوقفة
    }
    
    @Override
    public void render(GL2 gl, Game game) {
        if (playState != null) {
            playState.render(gl, game);
        }
        game.renderPaused(gl);
    }
    
    @Override
    public void keyPressed(KeyEvent e, Game game) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            // العودة إلى PlayState
            game.getStateManager().setState(playState);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e, Game game) {
        // لا شيء
    }
}