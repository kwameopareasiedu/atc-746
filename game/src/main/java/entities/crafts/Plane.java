package entities.crafts;

import dev.gamekit.components.BoxCollider;
import dev.gamekit.components.Sprite;
import dev.gamekit.core.Component;
import dev.gamekit.core.IO;
import dev.gamekit.utils.Vector;
import entities.behaviors.RunwayLander;
import entities.landing.Runway;
import utils.Physic;

import java.awt.image.BufferedImage;
import java.util.List;

public class Plane extends Craft implements RunwayLander {
  private static final BufferedImage SPRITE = IO.getResourceImage("plane.png");

  public Plane(Vector initialPosition, double initialHeading, Host host) {
    super("Plane", initialPosition, initialHeading, host);
  }

  @Override
  protected List<Component> getComponents() {
    List<Component> components = super.getComponents();

    Sprite sprite = new Sprite(SPRITE);
    sprite.setWidth(96);
    components.add(sprite);

    BoxCollider bodyCollider = new BoxCollider(18, 72);

    configureCollider(bodyCollider, (otherCollider) -> {
      if (otherCollider.getMetaData().equals(Physic.Tag.AIRSTRIP)) {
        land(Plane.this, (Runway) otherCollider.getEntity());
      }
    });

    components.add(bodyCollider);

    BoxCollider wingCollider = new BoxCollider(100, 14);
    configureCollider(wingCollider);
    components.add(wingCollider);

    return components;
  }

  @Override
  protected Parameters getParameters() {
    return new Parameters(1, 0.025, 192);
  }
}
