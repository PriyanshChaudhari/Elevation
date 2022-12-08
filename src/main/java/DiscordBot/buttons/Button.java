package DiscordBot.buttons;

import DiscordBot.DiscordBot;
import DiscordBot.commands.Command;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public abstract class Button extends Command {

  public Button(DiscordBot bot) {
    super(bot);
  }

  public abstract void execute(ButtonInteractionEvent event);
}