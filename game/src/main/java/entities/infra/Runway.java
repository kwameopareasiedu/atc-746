package entities.infra;

import dev.gamekit.components.BoxCollider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.components.Sprite;
import dev.gamekit.core.Component;
import dev.gamekit.core.Entity;
import dev.gamekit.core.IO;
import utils.Physic;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Runway extends Entity {
  private static final BufferedImage SPRITE = IO.getResourceImage("airstrip.png");

  public Runway() {
    super("Runway");
  }

  @Override
  protected List<Component> getComponents() {
    List<Component> components = new ArrayList<>();

    RigidBody rb = new RigidBody();
    components.add(rb);

    Sprite sprite = new Sprite(SPRITE);
    sprite.setWidth(72);
    components.add(sprite);

    BoxCollider collider = new BoxCollider(56, 284);
    collider.setMetaData(Physic.Tag.RUNWAY_STRIP);
    collider.setCollisionFilter(Physic.CategoryMask.RUNWAY_STRIP, Physic.CollisionMask.RUNWAY_STRIP);
    components.add(collider);

    return components;
  }
}
