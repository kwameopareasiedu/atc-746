package scenes;

import dev.gamekit.components.Collider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import prefabs.Plane;

import java.awt.*;

public class TestScene extends Scene {
  public TestScene() {
    super("Test Scene");

    RigidBody.DEBUG_DRAW = true;
    Collider.DEBUG_DRAW = true;
  }

  @Override
  protected void start() {
    addChild(new Plane());
  }

  @Override
  protected void render() {
    Renderer.clear(Color.BLACK);
  }
}
