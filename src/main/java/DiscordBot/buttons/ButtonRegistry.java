package DiscordBot.buttons;

import DiscordBot.DiscordBot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonRegistry extends ListenerAdapter {

  public static final List<Button> buttons = new ArrayList<>();

  public static final Map<String, Button> buttonsMap = new HashMap<>();

  public ButtonRegistry(DiscordBot bot) {
    mapButtons(
        new Skip(bot),
      new Pause(bot),
      new Repeat(bot),
      new VolumeUp(bot),
      new VolumeDown(bot)
    );
  }

  private void mapButtons(Button...btns){
    for (Button btn : btns){
      buttonsMap.put(btn.name, btn);
      buttons.add(btn);
    }
  }

  @Override
  public void onButtonInteraction(ButtonInteractionEvent event) {
    Button btn = buttonsMap.get(event.getButton().getId());
    if (btn != null){
      btn.execute(event);
    }
  }
}
