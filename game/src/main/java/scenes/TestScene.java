package scenes;

import dev.gamekit.components.Collider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.core.Application;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.utils.Vector;
import entities.Enclosure;
import entities.Explosion;
import entities.crafts.*;
import entities.landing.*;

import java.awt.*;

import static dev.gamekit.utils.Math.degToRad;

public class TestScene extends Scene implements Craft.Host {
  private final Enclosure enclosure;

  public TestScene() {
    super("Test Scene");

    enclosure = new Enclosure(this, Plane::new, Heli::new, Jet::new, Seaplane::new, Blimp::new);

    RigidBody.DEBUG_DRAW = true;
    Collider.DEBUG_DRAW = true;
  }

  @Override
  protected void start() {
    addChild(enclosure);
    addChild(new Airstrip(new Vector(-256, 0), degToRad(30)));
    addChild(new Jetstrip(new Vector(256, 0), degToRad(105)));
    addChild(new Helipad(new Vector(), 0));
    addChild(new Blimpad(new Vector(0, -256), degToRad(105)));
    addChild(new Seaport(new Vector(0, 256), degToRad(30)));
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 2500);
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 3500);
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 5000);
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 6500);
    Application.getInstance().scheduleTask(() -> addChild(enclosure.spawnCraft()), 8000);
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
  public void onCraftCrash(Vector location) {
    logger.debug("Crafts crashed");
    Craft.ENABLED = false;

    addChild(new Explosion(location));
  }

  @Override
  public void onCraftLanded() {
    logger.debug("Crafts landed");
  }
}
