package javaapplication9;

import com.jogamp.opengl.GL2;
import java.awt.event.KeyEvent;

public class MenuState implements GameState {
    private int menuSelection = 0;

    @Override
    public void update(Game game) {
        // لا شيء في القائمة
    }

    @Override
    public void render(GL2 gl, Game game) {
        game.renderMenu(gl);
    }

  // MenuState.java - حدث دالة keyPressed
@Override
public void keyPressed(KeyEvent e, Game game) {
    int previousSelection = menuSelection;
    
    switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
            menuSelection = (menuSelection == 0) ? 1 : 0;
            if (previousSelection != menuSelection) {
           //sound if change
                 java.awt.Toolkit.getDefaultToolkit().beep();
            }
            game.setMenuSelection(menuSelection);
            break;
            
        case KeyEvent.VK_ENTER:
            if (menuSelection == 0) {
                Sound.play("start.wav");
                game.initGame();
                game.getStateManager().setState(new PlayState(game.getGameWidth(), game.getGameHeight()));
            } else {
                System.exit(0);
            }
            break;
            
        case KeyEvent.VK_ESCAPE:
            System.exit(0);
            break;
    }
}
      @Override
    public void keyReleased(KeyEvent e, Game game) {
        // لا شيء
    }
}