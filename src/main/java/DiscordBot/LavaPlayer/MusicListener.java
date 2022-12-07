package DiscordBot.LavaPlayer;

import DiscordBot.BotUtilities.ButtonManager;
import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.BotUtilities.GuildData;
import DiscordBot.BotUtilities.SecurityUtils;
import com.github.topislavalinkplugins.topissourcemanagers.spotify.SpotifyConfig;
import com.github.topislavalinkplugins.topissourcemanagers.spotify.SpotifySourceManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.net.MalformedURLException;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class MusicListener extends ListenerAdapter {

  private final @NotNull AudioPlayerManager playerManager;

  public MusicListener(String spotifyClientId, String spotifyClientSecret) {
    this.playerManager = new DefaultAudioPlayerManager();

    // Add Spotify support
    SpotifyConfig spotifyConfig = new SpotifyConfig();
    spotifyConfig.setClientId(spotifyClientId);
    spotifyConfig.setClientSecret(spotifyClientSecret);
    spotifyConfig.setCountryCode("IN");
    this.playerManager.registerSourceManager(
        new SpotifySourceManager(null, spotifyConfig, playerManager));

    // Add audio player to source manager
    AudioSourceManagers.registerRemoteSources(playerManager);
  }

  @Nullable
  public MusicHandler getMusic(@NotNull SlashCommandInteractionEvent event,
      boolean skipQueueCheck) {
    GuildData settings = GuildData.get(event.getGuild());
    // Check if user is in voice channel
    if (!inChannel(Objects.requireNonNull(event.getMember()))) {
      String message = "Please connect to a voice channel first!";

      event.replyEmbeds(EmbedManager.default01(message))
          .addActionRow(ButtonManager.joinVC(event)).setEphemeral(true).queue();

      return null;
    }
    // Bot should join voice channel if not already in one.
    AudioChannel channel = Objects.requireNonNull(event.getMember().getVoiceState()).getChannel();
    if (settings.musicHandler == null || !event.getGuild().getAudioManager().isConnected()) {
      assert channel != null;
      joinChannel(settings, channel, event.getChannel().asTextChannel());
    }
    // Check if music is playing in this guild
    if (!skipQueueCheck) {
      if (settings.musicHandler == null || settings.musicHandler.getQueue().isEmpty()) {
        String message = "There are no songs in the queue!";
        event.replyEmbeds(EmbedManager.createError(message)).queue();
        return null;
      }
      // Check if member is in the right voice channel
      if (settings.musicHandler.getPlayChannel() != channel) {
        String message = "You are not in the same voice channel as the bot is";
        String title = "Different Voice Channel";

        event.replyEmbeds(EmbedManager.default01(title, message))
            .addActionRow(ButtonManager.joinVC(event)
                .withUrl(settings.musicHandler.getPlayChannel().getJumpUrl())
                .withLabel("Go to that Channel"))
            .setEphemeral(true).queue();
        return null;
      }
    }
    return settings.musicHandler;
  }

  public MusicHandler getMusic(@NotNull ButtonInteractionEvent event, boolean skipQueueCheck) {
    GuildData settings = GuildData.get(event.getGuild());
    // Check if user is in voice channel
    if (!inChannel(Objects.requireNonNull(event.getMember()))) {
      String message = "Please connect to a voice channel first!";
      event.replyEmbeds(EmbedManager.default01(message))
          .addActionRow(ButtonManager.joinVC(event)).setEphemeral(true).queue();

      return null;
    }
    // Bot should join voice channel if not already in one.
    AudioChannel channel = Objects.requireNonNull(event.getMember().getVoiceState()).getChannel();
    if (settings.musicHandler == null || !event.getGuild().getAudioManager().isConnected()) {
      assert channel != null;
      joinChannel(settings, channel, event.getChannel().asTextChannel());
    }
    // Check if music is playing in this guild
    if (!skipQueueCheck) {
      if (settings.musicHandler == null || settings.musicHandler.getQueue().isEmpty()) {
        String message = "There are no songs in the queue!";
        event.replyEmbeds(EmbedManager.createError(message)).queue();
        return null;
      }
      // Check if member is in the right voice channel
      if (settings.musicHandler.getPlayChannel() != channel) {
        String message = "You are not in the same voice channel as the bot is";
        String title = "Different Voice Channel";
        event.replyEmbeds(EmbedManager.default01(title, message))
            .addActionRow(ButtonManager.joinVC(event)
                .withUrl(settings.musicHandler.getPlayChannel().getJumpUrl())
                .withLabel("Go to that Channel"))
            .setEphemeral(true).queue();
        return null;
      }
    }
    return settings.musicHandler;
  }


  public void joinChannel(@NotNull GuildData guildData, @NotNull AudioChannel channel,
      TextChannel logChannel) {
    AudioManager manager = channel.getGuild().getAudioManager();
    if (guildData.musicHandler == null) {
      guildData.musicHandler = new MusicHandler(playerManager.createPlayer());
    }
    manager.setSendingHandler(guildData.musicHandler);
    Objects.requireNonNull(guildData.musicHandler).setLogChannel(logChannel);
    guildData.musicHandler.setPlayChannel(channel);
    manager.openAudioConnection(channel);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean inChannel(@NotNull Member member) {
    return member.getVoiceState() != null && member.getVoiceState().inAudioChannel();
  }


  public void addTrack(SlashCommandInteractionEvent event, String url, String userID) {
    MusicHandler music = GuildData.get(event.getGuild()).musicHandler;
    if (music == null) {
      return;
    }

    // Check for SSRF vulnerability with whitelist
    try {
      boolean isWhitelisted = SecurityUtils.isUrlWhitelisted(url);
      if (!isWhitelisted) {
        url = "";
      }
    } catch (MalformedURLException ignored) {
    }
    playerManager.loadItem(url, new AudioLoadResultHandler() {

      @Override
      public void trackLoaded(@NotNull AudioTrack audioTrack) {
        audioTrack.setUserData(userID);
        music.enqueue(audioTrack);
        event.reply("Added **" + audioTrack.getInfo().title + "** to the queue.").queue();
      }

      @Override
      public void playlistLoaded(@NotNull AudioPlaylist audioPlaylist) {
        // Queue first result if youtube search
        if (audioPlaylist.isSearchResult()) {
          trackLoaded(audioPlaylist.getTracks().get(0));
          return;
        }

        // Otherwise load first 100 tracks from playlist
        int total = audioPlaylist.getTracks().size();
        if (total > 100) {
          total = 100;
        }
        event.reply(
                "Added **" + audioPlaylist.getName() + "** with `" + total + "` songs to the queue.")
            .queue();

        total = music.getQueue().size();
        for (AudioTrack track : audioPlaylist.getTracks()) {
          if (total < 100) {
            music.enqueue(track);
          }
          total++;
        }
      }

      @Override
      public void noMatches() {
        String message = "That is not a valid song!";
        event.replyEmbeds(EmbedManager.createError(message)).setEphemeral(true).queue();
      }

      @Override
      public void loadFailed(FriendlyException e) {
        String message = "That is not a valid link!";
        event.replyEmbeds(EmbedManager.createError(message)).setEphemeral(true).queue();
      }
    });
  }
}