package javaapplication9;

import com.jogamp.opengl.GL2;
import java.awt.event.KeyEvent;

public class StateManager {
    private GameState currentState;

    public void setState(GameState state) {
        this.currentState = state;
    }

    public void update(Game game) {
        if (currentState != null) {
            currentState.update(game);
        }
    }

    public void render(GL2 gl, Game game) {
        if (currentState != null) {
            currentState.render(gl, game);
        }
    }

    public void keyPressed(KeyEvent e, Game game) {
        if (currentState != null) {
            currentState.keyPressed(e, game);
        }
    }
    
  public void keyReleased(KeyEvent e, Game game) {
    if (currentState != null) {
        currentState.keyReleased(e, game);
    }
}
}