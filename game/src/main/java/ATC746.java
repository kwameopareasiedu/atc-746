import dev.gamekit.core.Application;
import dev.gamekit.settings.*;
import scenes.TestScene;

public class ATC746 extends Application {
  public ATC746() {
    super(
      new Settings(
        "ATC 746",
        Resolution.FULL_HD,
        Antialiasing.ON,
        TextAntialiasing.ON,
        RenderingStrategy.QUALITY
      )
    );
  }

  public static void main(String[] args) {
    final ATC746 game = new ATC746();
    game.loadScene(new TestScene());
    game.run();
  }
}
