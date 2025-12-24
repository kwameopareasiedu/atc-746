package entities.crafts;

import dev.gamekit.components.CircleCollider;
import dev.gamekit.components.Sprite;
import dev.gamekit.components.Transform;
import dev.gamekit.core.Component;
import dev.gamekit.core.Entity;
import dev.gamekit.core.IO;
import dev.gamekit.utils.Math;
import dev.gamekit.utils.Vector;

import java.awt.image.BufferedImage;
import java.util.List;

public class Heli extends Craft {
  private static final BufferedImage BODY_SPRITE = IO.getResourceImage("heli_body.png");

  public Heli(Vector initialPosition, double initialHeading, Host host) {
    super("Plane", initialPosition, initialHeading, host);
  }

  @Override
  protected List<Component> getComponents() {
    List<Component> components = super.getComponents();

    Sprite bodySprite = new Sprite(BODY_SPRITE);
    CircleCollider bodyCollider = new CircleCollider(36);

    bodySprite.setWidth(48);
    bodySprite.setCenter(0, -12);

    configureCollider(bodyCollider);

    components.add(bodySprite);
    components.add(bodyCollider);

    return components;
  }

  @Override
  protected void start() {
    addChild(new Blades());
  }

  @Override
  protected Parameters getParameters() {
    return new Parameters(0.7, 0.025, 160);
  }

  private static class Blades extends Entity {
    private static final BufferedImage BLADES_SPRITE = IO.getResourceImage("heli_blade.png");
    private static final double SPIN_SPEED = 0.035;

    private double rotation = 0;

    public Blades() {
      super("Helo Blades");
    }

    @Override
    protected List<Component> getComponents() {
      Sprite bladesSprite = new Sprite(BLADES_SPRITE);

      bladesSprite.setWidth(128);

      return List.of(bladesSprite);
    }

    @Override
    protected void update() {
      Transform tx = findComponent(Transform.class);
      rotation = (rotation + SPIN_SPEED) % Math.TWO_PI;
      tx.setGlobalRotation(rotation);
    }
  }
}
