package DiscordBot.commands;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.BotUtilities.GuildData;
import DiscordBot.BotUtilities.MessageBuilderManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SuppressWarnings("ConstantConditions")
public class NowPlayingCommand extends Command {

  public NowPlayingCommand(DiscordBot bot) {
    super(bot);
    this.name = "playing";
    this.description = "Check which song is currently playing.";
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    // Verify the Music Manager isn't null.
    MusicHandler music = GuildData.get(event.getGuild()).musicHandler;
    if (music == null) {
      String message = "Not currently playing any music!";
      String title = "Not Playing";
      event.replyEmbeds(EmbedManager.default01(title, message)).setEphemeral(true).queue();
      return;
    }

    // Get currently playing track
    AudioTrack nowPlaying = music.getQueue().size() > 0 ? music.getQueue().getFirst() : null;
    if (nowPlaying == null) {
      String message = "Not currently playing any music!";
      String title = "Not Playing";
      event.replyEmbeds(EmbedManager.default01(title, message)).queue();
      return;
    }
    event.reply(MessageBuilderManager.trackInfo(nowPlaying, music)).queue();
  }
}