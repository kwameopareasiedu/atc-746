package scenes;

import dev.gamekit.components.Collider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.core.Application;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import models.Craft;
import prefabs.CraftSpawner;
import prefabs.Plane;

import java.awt.*;

public class TestScene extends Scene implements Craft.Host {
  private final CraftSpawner craftSpawner;

  public TestScene() {
    super("Test Scene");

    craftSpawner = new CraftSpawner(this, Plane::new);

    RigidBody.DEBUG_DRAW = true;
    Collider.DEBUG_DRAW = true;
  }

  @Override
  protected void start() {
    Application.getInstance().scheduleTask(() -> addChild(craftSpawner.spawnCraft()), 2500);
    Application.getInstance().scheduleTask(() -> addChild(craftSpawner.spawnCraft()), 3500);
  }

  @Override
  protected void render() {
    Renderer.clear(Color.BLACK);
  }

  @Override
  public void onCraftNearMiss() {
    logger.debug("Crafts missed nearly");
  }

  @Override
  public void onCraftCrash() {
    logger.debug("Crafts crashed");
  }
}
