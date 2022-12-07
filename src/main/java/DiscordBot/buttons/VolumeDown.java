package DiscordBot.buttons;

import DiscordBot.BotUtilities.ButtonManager;
import DiscordBot.BotUtilities.GuildData;
import DiscordBot.BotUtilities.MessageBuilderManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import DiscordBot.LavaPlayer.MusicListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

@SuppressWarnings("ConstantConditions")
public class VolumeDown extends Button {

  public VolumeDown(DiscordBot bot) {
    super(bot);
    this.name = "vol-down";
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {

  }

  @Override
  public void execute(ButtonInteractionEvent event) {
    MusicListener music = bot.musicListener;
    MusicHandler musicHandler = GuildData.get(event.getGuild()).musicHandler;
    AudioTrack nowPlaying =
        musicHandler.getQueue().size() > 0 ? musicHandler.getQueue().getFirst() : null;
    int volume = (music.getMusic(event, true).audioPlayer.getVolume());

    if (volume >= 10) {
      if (event.getMessage().getButtonById("vol-down").getStyle()==ButtonStyle.SUCCESS
          || event.getMessage().getButtonById("vol-down").isDisabled()){
        event.getMessage().editMessage(MessageEditData.fromCreateData(MessageBuilderManager.trackInfo(nowPlaying, musicHandler))).queue();
      }
      music.getMusic(event, true)
          .setVolume(volume - 10);
      event.getMessage().editMessage(
              MessageEditData.fromCreateData(
                  MessageBuilderManager.trackInfo(nowPlaying, musicHandler)))
          .and(event.getInteraction().editButton(ButtonManager.volumeDown()))
          .queue();
    }
    if (volume < 10) {
      event.getMessage().editMessage(
              MessageEditData.fromCreateData(MessageBuilderManager.trackInfo(nowPlaying, musicHandler)))
          .and(event.getInteraction()
              .editButton(ButtonManager.volumeDown().withLabel("Min Volume").withStyle(
                  ButtonStyle.SUCCESS).asDisabled())).queue();
    }
  }
}