package entities.crafts;

import dev.gamekit.components.BoxCollider;
import dev.gamekit.components.Collider;
import dev.gamekit.components.Sprite;
import dev.gamekit.core.Component;
import dev.gamekit.core.IO;
import dev.gamekit.core.Physics;
import dev.gamekit.utils.Vector;
import utils.Physic;

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
    bodyCollider.setMetaData(Physic.Tag.CRAFT_BODY);
    bodyCollider.setCollisionFilter(Physic.CategoryMask.CRAFT_BODY, Physic.CollisionMask.CRAFT_BODY);
    bodyCollider.setCollisionListener(new Physics.CollisionListener() {
      @Override
      public void onCollisionEnter(Collider otherCollider) {
        if (otherCollider.getMetaData().equals(Physic.Tag.CRAFT_BODY))
          host.onCraftCrash();
        else if (otherCollider.getMetaData().equals(Physic.Tag.DESTROYER))
          destroy();
      }
    });

    bodyCollider.setSensor(true);
    wingCollider.setMetaData(Physic.Tag.CRAFT_BODY);
    wingCollider.setCollisionFilter(Physic.CategoryMask.CRAFT_BODY, Physic.CollisionMask.CRAFT_BODY);
    wingCollider.setCollisionListener(new Physics.CollisionListener() {
      @Override
      public void onCollisionEnter(Collider otherCollider) {
        if (otherCollider.getMetaData().equals(Physic.Tag.CRAFT_BODY))
          host.onCraftCrash();
        else if (otherCollider.getMetaData().equals(Physic.Tag.DESTROYER))
          destroy();
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
