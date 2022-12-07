package DiscordBot.commands;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.BotUtilities.GuildData;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ConstantConditions")
public class QueueCommand extends Command {

  public static final int SONGS_PER_PAGE = 7;

  public QueueCommand(DiscordBot bot) {
    super(bot);
    this.name = "queue";
    this.description = "Display the current queue of songs.";
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    MusicHandler music = GuildData.get(event.getGuild()).musicHandler;

    // Check if queue is null or empty
    if (music == null || music.getQueue().isEmpty()) {
      String message = "There are no songs in the queue!";
      event.replyEmbeds(EmbedManager.createError(message)).queue();
      return;
    }
    // Create embeds and send to channel
    List<MessageEmbed> embeds = buildQueueEmbeds(music.getQueue(), music.getQueue().size());
    ReplyCallbackAction action = event.replyEmbeds(embeds.get(0));
    action.queue();
  }

  private @NotNull List<MessageEmbed> buildQueueEmbeds(@NotNull LinkedList<AudioTrack> queue,
      int queueSize) {
    int count = 0;
    StringBuilder description = new StringBuilder();
    List<MessageEmbed> embeds = new ArrayList<>();
    EmbedBuilder embed = new EmbedBuilder()
        .setColor(0x57a2ff)
        .setTitle("Music PlayList");

    String song = "Song";
    if (queueSize >= 3) {
      song += "s";
    }
    String footer = queueSize > 1 ? String.format("\n**%s %s in Queue**", queueSize - 1, song) : "";

    for (AudioTrack track : queue) {
      AudioTrackInfo trackInfo = track.getInfo();
      if (count == 0) { //Current playing track
        description.append("__Now Playing:__\n");
        description.append(String.format("[%s](%s) ", trackInfo.title, trackInfo.uri));
        count++;
        continue;
      } else if (count == 1) { //Header for queue
        description.append("\n__Up Next:__\n");
      }
      //Rest of the queue
      description.append(String.format("`%s.` [%s](%s) \n", count, trackInfo.title, trackInfo.uri));
      count++;
      if (count % SONGS_PER_PAGE == 0) {
        // Add embed as new page
        description.append(footer);
        embed.setDescription(description.toString());
        embeds.add(embed.build());
        description = new StringBuilder();
      }
    }
    if (count % SONGS_PER_PAGE != 0) {
      description.append(footer);
      embed.setDescription(description.toString());
      embeds.add(embed.build());
    }
    return embeds;
  }
}
