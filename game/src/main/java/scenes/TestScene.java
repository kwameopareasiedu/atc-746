package scenes;

import dev.gamekit.components.Collider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.core.Application;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import prefabs.CraftSpawner;
import prefabs.Plane;

import java.awt.*;

public class TestScene extends Scene {
  private final CraftSpawner craftSpawner = new CraftSpawner(Plane::new);

  public TestScene() {
    super("Test Scene");

    RigidBody.DEBUG_DRAW = true;
    Collider.DEBUG_DRAW = true;
  }

  @Override
  protected void start() {
    Application.getInstance().scheduleTask(() -> addChild(craftSpawner.spawnCraft()), 2500);
  }

  @Override
  protected void render() {
    Renderer.clear(Color.BLACK);
  }
}
