package entities.crafts;

import dev.gamekit.components.CircleCollider;
import dev.gamekit.components.Sprite;
import dev.gamekit.core.Component;
import dev.gamekit.core.IO;
import dev.gamekit.settings.ImageInterpolation;
import dev.gamekit.utils.Vector;
import entities.behaviors.VerticalLander;
import entities.landing.Pad;
import utils.Physic;

import java.awt.image.BufferedImage;
import java.util.List;

public class Blimp extends Craft implements VerticalLander {
  private static final BufferedImage BODY_SPRITE = IO.getResourceImage("blimp.png");

  public Blimp(Vector initialPosition, double initialHeading, Host host) {
    super("Blimp", initialPosition, initialHeading, host);
  }

  @Override
  protected List<Component> getComponents() {
    List<Component> components = super.getComponents();

    Sprite bodySprite = new Sprite(BODY_SPRITE, ImageInterpolation.BICUBIC);
    bodySprite.setWidth(48);
    bodySprite.setOffset(0, -12);
    components.add(bodySprite);

    CircleCollider bodyCollider = new CircleCollider(36);
    bodyCollider.setOffset(0, -12);
    configureCollider(bodyCollider, otherCollider -> {
      if (otherCollider.getMetaData().equals(Physic.Tag.BLIMPAD)) {
        land(Blimp.this, (Pad) otherCollider.getEntity());
      }
    });
    components.add(bodyCollider);

    return components;
  }

  @Override
  protected Parameters getParameters() {
    return new Parameters(0.5, 0.015, 175);
  }
}
