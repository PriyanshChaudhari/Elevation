package DiscordBot.buttons;

import DiscordBot.BotUtilities.ButtonManager;
import DiscordBot.BotUtilities.MessageBuilderManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

@SuppressWarnings("ConstantConditions")
public class Repeat extends Button {

  public Repeat(DiscordBot bot) {
    super(bot);
    this.name = "repeat";
  }

  @Override
  public void execute(ButtonInteractionEvent event) {
    MusicHandler music = bot.musicListener.getMusic(event, false);
    AudioTrack nowPlaying = music.getQueue().size() > 0 ? music.getQueue().getFirst() : null;
    if (music == null) {
      return;
    }

    music.loop();
    event.getMessage().editMessage(
        MessageEditData.fromCreateData(MessageBuilderManager.trackInfo(nowPlaying, music))).queue();
    if (music.isLoop()) {
      event.getInteraction().editButton(ButtonManager.repeat().withStyle(ButtonStyle.SUCCESS))
          .queue();
    } else {
      event.getInteraction().editButton(ButtonManager.repeat().withStyle(ButtonStyle.PRIMARY))
          .queue();
    }
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
  }
}