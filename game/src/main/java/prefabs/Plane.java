package prefabs;

import dev.gamekit.components.BoxCollider;
import dev.gamekit.components.Collider;
import dev.gamekit.components.Sprite;
import dev.gamekit.core.Component;
import dev.gamekit.core.IO;
import dev.gamekit.core.Physics;
import dev.gamekit.utils.Vector;
import models.Craft;

import java.awt.image.BufferedImage;
import java.util.List;

public class Plane extends Craft {
  private static final BufferedImage SPRITE = IO.getResourceImage("plane.png");

  public Plane(Vector initialPosition, double initialHeading, Host host) {
    super("Plane", initialPosition, initialHeading, host);
  }

  @Override
  protected void setupAdditionalComponents(List<Component> components) {
    Sprite sprite = new Sprite(SPRITE);
    BoxCollider bodyCollider = new BoxCollider(18, 58);
    BoxCollider wingCollider = new BoxCollider(120, 14);

    sprite.setCenter(-3, -12);
    sprite.setWidth(128);

    bodyCollider.setSensor(true);
    bodyCollider.setOffset(0, -12);
    bodyCollider.setMetaData(BODY_COLLIDER_TAG);
    bodyCollider.setCollisionFilter(BODY_COLLIDER_LAYER_MASK, BODY_COLLIDER_LAYER_MASK | PROXIMITY_SENSOR_LAYER_MASK);
    bodyCollider.setCollisionListener(new Physics.CollisionListener() {
      @Override
      public void onCollisionEnter(Collider otherCollider) {
        if (otherCollider.getMetaData().equals(BODY_COLLIDER_TAG))
          host.onCraftCrash();
      }
    });

    bodyCollider.setSensor(true);
    wingCollider.setMetaData(BODY_COLLIDER_TAG);
    wingCollider.setCollisionFilter(BODY_COLLIDER_LAYER_MASK, BODY_COLLIDER_LAYER_MASK | PROXIMITY_SENSOR_LAYER_MASK);
    wingCollider.setCollisionListener(new Physics.CollisionListener() {
      @Override
      public void onCollisionEnter(Collider otherCollider) {
        if (otherCollider.getMetaData().equals(BODY_COLLIDER_TAG))
          host.onCraftCrash();
      }
    });

    components.add(sprite);
    components.add(bodyCollider);
    components.add(wingCollider);
  }

  @Override
  protected double getMoveSpeed() {
    return 1;
  }

  @Override
  protected double getTurnSpeed() {
    return 0.025;
  }
}
