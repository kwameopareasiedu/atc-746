package scenes;

import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.widgets.*;
import prefabs.PathTracer;

import java.awt.*;

public class TestScene extends Scene {
  public TestScene() {
    super("Test Scene");
  }

  @Override
  protected void start() {
    addChild(PathTracer.getInstance());
  }

  @Override
  protected void render() {
    Renderer.clear(Color.BLACK);
  }

  @Override
  protected Widget createUI() {
    return Center.create(
      Text.create(
        TextConfig.text("ATC-746")
      )
    );
  }
}
