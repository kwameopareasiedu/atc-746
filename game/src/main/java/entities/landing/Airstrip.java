package entities.landing;

import dev.gamekit.core.IO;
import dev.gamekit.utils.Vector;
import utils.Physic;

import java.awt.image.BufferedImage;

public class Airstrip extends Runway {
  private static final BufferedImage SPRITE = IO.getResourceImage("airstrip.png");

  public Airstrip(Vector initialPosition, double initialRotation) {
    super("Airstrip", initialPosition, initialRotation);
  }

  @Override
  protected Parameters getParameters() {
    return new Parameters(SPRITE, Physic.Tag.AIRSTRIP, Physic.CategoryMask.AIRSTRIP, Physic.CollisionMask.AIRSTRIP);
  }
}
