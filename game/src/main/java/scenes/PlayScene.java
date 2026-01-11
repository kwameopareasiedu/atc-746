package scenes;

import dev.gamekit.core.Application;
import dev.gamekit.core.Input;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.settings.Settings;
import dev.gamekit.settings.WindowMode;
import dev.gamekit.ui.widgets.Empty;
import dev.gamekit.ui.widgets.Stack;
import dev.gamekit.ui.widgets.Widget;
import ui.LevelSuccessPopup;

import java.awt.*;

public class PlayScene extends Scene {
  private boolean started = false;

  public PlayScene() {
    super("Play");

    Widget.DEBUG_DRAW = true;
  }

  public static void main(String[] args) {
    Application game = new Application(
      new Settings("Play", WindowMode.BORDERLESS)
    ) { };
    game.loadScene(new PlayScene());
    game.run();
  }

  @Override
  protected void update() {
    if (Input.isKeyDown(Input.KEY_SPACE)) {
      started = !started;
      updateUI();
    }
  }

  @Override
  protected void render() {
    Renderer.clear(Color.BLACK);
  }

  @Override
  protected Widget createUI() {
    return Stack.create(
      started ? LevelSuccessPopup.create() : Empty.create()
    );
  }
}
