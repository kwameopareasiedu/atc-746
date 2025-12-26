package entities.crafts;

import dev.gamekit.components.CircleCollider;
import dev.gamekit.components.Sprite;
import dev.gamekit.components.Transform;
import dev.gamekit.core.Component;
import dev.gamekit.core.Entity;
import dev.gamekit.core.IO;
import dev.gamekit.utils.Math;
import dev.gamekit.utils.Vector;
import entities.behaviors.VerticalLander;
import entities.landing.Pad;
import utils.Physic;

import java.awt.image.BufferedImage;
import java.util.List;

public class Heli extends Craft implements VerticalLander {
  private static final BufferedImage BODY_SPRITE = IO.getResourceImage("heli_body.png");

  public Heli(Vector initialPosition, double initialHeading, Host host) {
    super("Heli", initialPosition, initialHeading, host);
  }

  @Override
  protected List<Component> getComponents() {
    List<Component> components = super.getComponents();

    Sprite bodySprite = new Sprite(BODY_SPRITE);
    bodySprite.setWidth(36);
    bodySprite.setOffset(0, -12);
    components.add(bodySprite);

    CircleCollider bodyCollider = new CircleCollider(36);
    configureCollider(bodyCollider, otherCollider -> {
      if (otherCollider.getMetaData().equals(Physic.Tag.HELIPAD)) {
        land(Heli.this, (Pad) otherCollider.getEntity());
      }
    });
    components.add(bodyCollider);

    return components;
  }

  @Override
  protected void start() {
    super.start();
    addChild(new Rotor());
  }

  @Override
  protected Parameters getParameters() {
    return new Parameters(0.7, 0.025, 160);
  }

  private static class Rotor extends Entity {
    private static final BufferedImage SPRITE = IO.getResourceImage("heli_blade.png");
    private static final double SPIN_SPEED = 0.035;

    private double rotation = 0;

    public Rotor() {
      super("Rotor");
    }

    @Override
    protected List<Component> getComponents() {
      Sprite bladesSprite = new Sprite(SPRITE);

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
