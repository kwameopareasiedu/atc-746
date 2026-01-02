package entities.landing;

import dev.gamekit.components.BoxCollider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.components.Sprite;
import dev.gamekit.core.Component;
import dev.gamekit.core.Entity;
import dev.gamekit.settings.ImageInterpolation;
import dev.gamekit.utils.Vector;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Runway extends Entity {
  private final Vector initialPosition;
  private final double initialRotation;

  public Runway(String name, Vector initialPosition, double initialRotation) {
    super(name);
    this.initialPosition = initialPosition;
    this.initialRotation = initialRotation;
  }

  @Override
  protected List<Component> getComponents() {
    List<Component> components = new ArrayList<>();
    Parameters parameters = getParameters();

    RigidBody rb = new RigidBody();
    components.add(rb);

    Sprite sprite = new Sprite(parameters.image, ImageInterpolation.BICUBIC);
    sprite.setWidth(72);
    components.add(sprite);

    BoxCollider collider = new BoxCollider(56, 284);
    collider.setMetaData(parameters.tag);
    collider.setCollisionFilter(parameters.categoryMask, parameters.collisionMask);
    components.add(collider);

    return components;
  }

  @Override
  protected void start() {
    RigidBody rb = findComponent(RigidBody.class);
    rb.setPosition(initialPosition.x, initialPosition.y);
    rb.setRotation(initialRotation);
  }

  protected abstract Parameters getParameters();

  protected record Parameters(BufferedImage image, String tag, int categoryMask, int collisionMask) { }
}
