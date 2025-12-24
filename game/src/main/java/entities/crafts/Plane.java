package entities.crafts;

import dev.gamekit.components.BoxCollider;
import dev.gamekit.components.Sprite;
import dev.gamekit.core.Component;
import dev.gamekit.core.IO;
import dev.gamekit.utils.Vector;

import java.awt.image.BufferedImage;
import java.util.List;

public class Plane extends Craft {
  private static final BufferedImage SPRITE = IO.getResourceImage("plane.png");

  public Plane(Vector initialPosition, double initialHeading, Host host) {
    super("Plane", initialPosition, initialHeading, host);
  }

  @Override
  protected List<Component> getComponents() {
    List<Component> components = super.getComponents();

    Sprite sprite = new Sprite(SPRITE);
    BoxCollider bodyCollider = new BoxCollider(18, 72);
    BoxCollider wingCollider = new BoxCollider(100, 14);

    sprite.setWidth(96);

    configureCollider(bodyCollider);
    configureCollider(wingCollider);

    components.add(sprite);
    components.add(bodyCollider);
    components.add(wingCollider);

    return components;
  }

  @Override
  protected Parameters getParameters() {
    return new Parameters(1, 0.025, 192);
  }
}
