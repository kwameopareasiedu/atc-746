package scenes;

import dev.gamekit.components.Collider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.core.Application;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.utils.Vector;
import entities.Enclosure;
import entities.crafts.Craft;
import entities.crafts.Heli;
import entities.crafts.Jet;
import entities.crafts.Plane;
import entities.infra.Runway;

import java.awt.*;

public class TestScene extends Scene implements Craft.Host {
  private final Enclosure enclosure;

  public TestScene() {
    super("Test Scene");

    enclosure = new Enclosure(this, Plane::new, Heli::new, Jet::new);

    RigidBody.DEBUG_DRAW = true;
    Collider.DEBUG_DRAW = true;
  }

  @Override
  protected void start() {
    addChild(enclosure);
    addChild(new Runway());
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 2500);
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 3500);
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 5000);
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

  @Override
  public void onCraftLanded() {
    logger.debug("Crafts landed");
  }
}
