package DiscordBot.commands;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class SkipCommand extends Command {

  public SkipCommand(DiscordBot bot) {
    super(bot);
    this.name = "skip";
    this.description = "Skip the current song.";
  }

  public void execute(SlashCommandInteractionEvent event) {
    MusicHandler music = bot.musicListener.getMusic(event, false);
    if (music == null) {
      return;
    }

    music.skipTrack();
    ReplyCallbackAction action = event.reply(":fast_forward: Skipping...");
    if (music.getQueue().size() == 1) {
      String text = "The music queue is now empty!";
      action = action.addEmbeds(EmbedManager.createError("Error!", text));
    }
    action.queue();
  }
}
