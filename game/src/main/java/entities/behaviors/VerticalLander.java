package entities.behaviors;

import dev.gamekit.animation.Animation;
import dev.gamekit.animation.AnimationCurve;
import dev.gamekit.components.RigidBody;
import dev.gamekit.components.Sprite;
import dev.gamekit.components.Transform;
import dev.gamekit.utils.Vector;
import entities.crafts.Craft;
import entities.landing.Pad;
import entities.landing.Runway;

import java.util.List;

public interface VerticalLander {
  default void land(Craft self, Pad pad) {
    Vector lastWaypoint = self.getLastWaypoint();
    if (lastWaypoint == null) return;

    RigidBody otherRb = pad.findComponent(RigidBody.class);
    if (!otherRb.containsPoint(lastWaypoint)) return;

    if (!self.hasBeganLandingSequence()) {
      List<Sprite> sprites = self.findComponents(Sprite.class);
      Animation fadeAnimation = new Animation(1000, Animation.RepeatMode.NONE, AnimationCurve.EASE_OUT_CUBIC);

      fadeAnimation.setValueListener(value -> {
        for (Sprite spr : sprites)
          spr.setOpacity(1.0 - value);
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
