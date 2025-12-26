package entities.behaviors;

import dev.gamekit.animation.Animation;
import dev.gamekit.animation.AnimationCurve;
import dev.gamekit.components.RigidBody;
import dev.gamekit.components.Sprite;
import dev.gamekit.components.Transform;
import dev.gamekit.utils.Vector;
import entities.crafts.Craft;
import entities.landing.Runway;

import java.util.List;

public interface RunwayLander {
  default void land(Craft self, Runway runway) {
    Vector lastWaypoint = self.getLastWaypoint();
    if (lastWaypoint == null) return;

    RigidBody otherRb = runway.findComponent(RigidBody.class);
    if (!otherRb.containsPoint(lastWaypoint)) return;

    Transform thisTx = self.findComponent(Transform.class);
    Transform otherTx = runway.findComponent(Transform.class);
    Vector thisHeading = Vector.from(1, thisTx.getGlobalRotation());
    Vector otherHeading = Vector.from(1, otherTx.getGlobalRotation());
    double dotProduct = Vector.dot(thisHeading, otherHeading);

    if (0.925 <= dotProduct && dotProduct <= 1 && !self.hasBeganLandingSequence()) {
      List<Sprite> sprites = self.findComponents(Sprite.class);
      Animation fadeAnimation = new Animation(1250, Animation.RepeatMode.NONE, AnimationCurve.EASE_OUT_CUBIC);

      fadeAnimation.setValueListener(value -> {
        for (Sprite sprite : sprites) {
          double scale = 1 - value * 0.5;
          sprite.setOpacity(1.0 - value);
          sprite.setScale(scale, scale);
        }
      });

      fadeAnimation.setStateListener(state -> {
        if (state == Animation.State.STOPPED)
          self.destroy();
      });

      fadeAnimation.start();
      self.beginLandingSequence();
    }
  }
}
