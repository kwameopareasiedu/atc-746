package entities.landing;

import dev.gamekit.components.BoxCollider;
import dev.gamekit.components.CircleCollider;
import dev.gamekit.components.RigidBody;
import dev.gamekit.components.Sprite;
import dev.gamekit.core.Component;
import dev.gamekit.core.Entity;
import dev.gamekit.utils.Vector;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Pad extends Entity {
  private final Vector initialPosition;
  private final double initialRotation;

  public Pad(String name, Vector initialPosition, double initialRotation) {
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

    Sprite sprite = new Sprite(parameters.image);
    sprite.setWidth(96);
    components.add(sprite);

    CircleCollider collider = new CircleCollider(36);
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
