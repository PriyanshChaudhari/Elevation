package DiscordBot.commands;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PauseCommand extends Command {

  public PauseCommand(DiscordBot bot) {
    super(bot);
    this.name = "pause";
    this.description = "Pause the current playing track.";
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    MusicHandler music = bot.musicListener.getMusic(event, false);
    if (music == null) return;

    if (music.isPaused()) {
      String message = "The player is already paused!";

      event.replyEmbeds(EmbedManager.default01(message)).setEphemeral(true).queue();
    } else {
      String message = ":pause_button: Paused the music player.";
      music.pause();
      event.replyEmbeds(EmbedManager.default01(message)).queue();
    }
  }
}
