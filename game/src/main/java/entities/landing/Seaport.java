package entities.landing;

import dev.gamekit.core.IO;
import dev.gamekit.utils.Vector;
import utils.Physic;

import java.awt.image.BufferedImage;

public class Seaport extends Runway {
  private static final BufferedImage SPRITE = IO.getResourceImage("seaport.png");

  public Seaport(Vector initialPosition, double initialRotation) {
    super("Seaport", initialPosition, initialRotation);
  }

  @Override
  protected Parameters getParameters() {
    return new Parameters(SPRITE, Physic.Tag.SEAPORT, Physic.CategoryMask.SEAPORT, Physic.CollisionMask.SEAPORT);
  }
}
