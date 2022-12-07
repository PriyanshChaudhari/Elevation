package DiscordBot.BotUtilities;

import DiscordBot.LavaPlayer.MusicHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class MessageBuilderManager {

  public static MessageCreateData trackInfo(AudioTrack track, MusicHandler handler) {
    if (handler.isLoop()) {
      return new MessageCreateBuilder()
          .addEmbeds(EmbedManager.displayTrack(track, handler))
          .addActionRow(ButtonManager.pause(),
              ButtonManager.repeat().withStyle(ButtonStyle.SUCCESS), ButtonManager.skip())
          .addActionRow(ButtonManager.volumeUp(), ButtonManager.volumeDown()).build();
    } else {
      return new MessageCreateBuilder()
          .addEmbeds(EmbedManager.displayTrack(track, handler))
          .addActionRow(ButtonManager.pause(), ButtonManager.repeat(), ButtonManager.skip())
          .addActionRow(ButtonManager.volumeUp(), ButtonManager.volumeDown()).build();
    }
  }
}