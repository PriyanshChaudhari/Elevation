package DiscordBot.LavaPlayer;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.BotUtilities.MessageBuilderManager;
import DiscordBot.BotUtilities.SecurityUtils;
import com.github.topislavalinkplugins.topissourcemanagers.ISRCAudioTrack;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MusicHandler implements AudioSendHandler {

  public final @NotNull AudioPlayer audioPlayer;
  public final @NotNull LinkedList<AudioTrack> queue;
  private AudioFrame lastFrame;
  private TextChannel logChannel;

  private @Nullable AudioChannel playChannel;

  private boolean isLoop;
  private boolean isSkip;

  public MusicHandler(@NotNull AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
    this.queue = new LinkedList<>();
    this.isLoop = false;
    this.isSkip = false;
    TrackScheduler scheduler = new TrackScheduler(this);
    audioPlayer.addListener(scheduler);
  }

  public static String getThumbnail(AudioTrack track) {
    String domain = SecurityUtils.getDomain(track.getInfo().uri);
    if (domain.equalsIgnoreCase("spotify") || domain.equalsIgnoreCase("apple")) {
      return ((ISRCAudioTrack) track).getArtworkURL();
    }
    return String.format("https://img.youtube.com/vi/%s/0.jpg", track.getIdentifier());
  }

  public void enqueue(AudioTrack track) {
    queue.addLast(track);
    if (audioPlayer.getPlayingTrack() == null) {
      audioPlayer.playTrack(queue.getFirst());
    }
  }

  public void pause() {
    audioPlayer.setPaused(true);
  }

  public void unpause() {
    audioPlayer.setPaused(false);
  }

  public boolean isPaused() {
    return audioPlayer.isPaused();
  }

  public void disconnect() {
    playChannel = null;
    queue.clear();
    audioPlayer.stopTrack();
  }

  public void setVolume(int volume) {
    audioPlayer.setVolume(volume);
  }

  public void skipTrack() {
    isSkip = true;
    audioPlayer.getPlayingTrack().setPosition(audioPlayer.getPlayingTrack().getDuration());
  }

  public @NotNull LinkedList<AudioTrack> getQueue() {
    return new LinkedList<>(queue);
  }

  public void setPlayChannel(@Nullable AudioChannel channel) {
    playChannel = channel;
  }

  public void setLogChannel(TextChannel channel) {
    logChannel = channel;
  }

  public @Nullable AudioChannel getPlayChannel() {
    return playChannel;
  }
  public boolean isLoop() {
    return isLoop;
  }

  public void loop() {
    isLoop = !isLoop;
  }

  @Override
  public boolean canProvide() {
    lastFrame = audioPlayer.provide();
    return lastFrame != null;
  }

  @Override
  public boolean isOpus() {
    return true;
  }

  @Nullable
  @Override
  public ByteBuffer provide20MsAudio() {
    return ByteBuffer.wrap(lastFrame.getData());
  }

  public static class TrackScheduler extends AudioEventAdapter {

    private final MusicHandler handler;

    public TrackScheduler(MusicHandler handler) {
      this.handler = handler;
    }


    @Override
    public void onTrackStart(AudioPlayer player, @NotNull AudioTrack track) {
      handler.logChannel.sendMessage(MessageBuilderManager.trackInfo(track, handler)).queue();
    }

    @Override
    public void onTrackEnd(@NotNull AudioPlayer player, @NotNull AudioTrack track,
        @NotNull AudioTrackEndReason endReason) {
      if (handler.isLoop() && !handler.isSkip) {
        // Loop current track
        handler.queue.set(0, track.makeClone());
        player.playTrack(handler.queue.getFirst());
      } else if (!handler.queue.isEmpty()) {
        // Play next track in queue
        handler.isSkip = false;
        handler.queue.removeFirst();
        if (endReason.mayStartNext && handler.queue.size() > 0) {
          player.playTrack(handler.queue.getFirst());
        }
      }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track,
        @NotNull FriendlyException exception) {
      String message = "An error occurred! " + exception.getMessage();
      handler.logChannel.sendMessageEmbeds(EmbedManager.createError(message)).queue();
      exception.printStackTrace();
    }
  }
}