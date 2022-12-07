package DiscordBot.commands;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ResumeCommand extends Command {

  public ResumeCommand(DiscordBot bot) {
    super(bot);
    this.name = "resume";
    this.description = "Resumes the current paused track.";
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    MusicHandler music = bot.musicListener.getMusic(event, false);
    if (music == null) {
      return;
    }

    if (music.isPaused()) {
      music.unpause();
      String text = ":arrow_forward: Resuming the music player.";
      event.replyEmbeds(EmbedManager.default01(text)).queue();
    } else {
      String text = "The player is not paused!";
      event.replyEmbeds(EmbedManager.createError(text)).setEphemeral(true).queue();
    }
  }
}
