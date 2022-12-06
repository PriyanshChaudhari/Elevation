package DiscordBot.commands;

import DiscordBot.BotUtilities.ButtonManager;
import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import java.net.MalformedURLException;
import java.net.URL;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@SuppressWarnings("ConstantConditions")
public class PlayCommand extends Command {

  public PlayCommand(DiscordBot bot) {
    super(bot);
    this.name = "play";
    this.description = "Add a song to the queue and play it.";
    this.args.add(
        new OptionData(OptionType.STRING, "song", "Song to search for or a link to the song",
            true));
  }

  public void execute(SlashCommandInteractionEvent event) {
    String song = event.getOption("song").getAsString();
    MusicHandler music = bot.musicListener.getMusic(event, true);
    if (music == null) {
      return;
    }

    AudioChannel channel = event.getMember().getVoiceState().getChannel();
    if (music.getPlayChannel() != channel) {
      String message = "You are not in the same voice channel as the bot is!";
      event.replyEmbeds((EmbedManager.createError("Error!", message)))
          .addActionRow(ButtonManager.joinVC(event)
              .withUrl(music.getPlayChannel().getJumpUrl())
              .withLabel("Go to that Channel"))
          .setEphemeral(true).queue();
      return;
    }

    // Cannot have more than 100 songs in the queue
    if (music.getQueue().size() >= 100) {
      String message = "You cannot queue more than 100 songs!";
      String title = "Error!";
      event.replyEmbeds(EmbedManager.createError(title, message)).setEphemeral(true).queue();
      return;
    }

    String userID = event.getUser().getId();
    try {
      String url;
      try {
        // Check for real URL
        url = new URL(song).toString();
      } catch (MalformedURLException e) {
        // Else search youtube using args
        url = "ytsearch:" + song;
        music.setLogChannel(event.getChannel().asTextChannel());
        bot.musicListener.addTrack(event, url, userID);
        return;
      }
      // Search youtube if using a soundcloud link
      if (url.contains("https://soundcloud.com/")) {
        String[] contents = url.split("/");
        url = "ytsearch:" + contents[3] + "/" + contents[4];
      }
      // Otherwise add real URL to queue
      music.setLogChannel(event.getChannel().asTextChannel());
      bot.musicListener.addTrack(event, url, userID);
    } catch (IndexOutOfBoundsException e) {
      String message = "Please specify a song a to play.";
      event.replyEmbeds(EmbedManager.createError(message)).setEphemeral(true).queue();
    }
  }
}