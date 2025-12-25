package entities.landing;

import dev.gamekit.core.IO;
import dev.gamekit.utils.Vector;
import utils.Physic;

import java.awt.image.BufferedImage;

public class Jetstrip extends Runway {
  private static final BufferedImage SPRITE = IO.getResourceImage("jetstrip.png");

  public Jetstrip(Vector initialPosition, double initialRotation) {
    super("Jetstrip", initialPosition, initialRotation);
  }

  @Override
  protected Parameters getParameters() {
    return new Parameters(SPRITE, Physic.Tag.JETSTRIP, Physic.CategoryMask.JETSTRIP, Physic.CollisionMask.JETSTRIP);
  }
}
