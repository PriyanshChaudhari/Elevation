package DiscordBot.commands;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class RepeatCommand extends Command {

  public RepeatCommand(DiscordBot bot) {
    super(bot);
    this.name = "repeat";
    this.description = "Toggles the repeat mode.";
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    MusicHandler music = bot.musicListener.getMusic(event, false);
    if (music == null) {
      return;
    }

    music.loop();
    String text;
    if (music.isLoop()) {
      text = ":repeat: Repeat has been enabled.";
    } else {
      text = ":repeat: Repeat has been disabled.";
    }
    event.replyEmbeds(EmbedManager.default01(text)).queue();
  }
}
