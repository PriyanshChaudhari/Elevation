package DiscordBot.buttons;

import DiscordBot.BotUtilities.ButtonManager;
import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.BotUtilities.MessageBuilderManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

@SuppressWarnings("ConstantConditions")
public class Pause extends Button {

  public Pause(DiscordBot bot) {
    super(bot);
    this.name = "pause";
  }

  @Override
  public void execute(ButtonInteractionEvent event) {
    MusicHandler music = bot.musicListener.getMusic(event, false);
    AudioTrack nowPlaying = music.getQueue().size() > 0 ? music.getQueue().getFirst() : null;
    if (music == null) {
      return;
    }

    if (music.isPaused()) {
      if (music.isPaused()) {
        music.unpause();
        event.getMessage().editMessage(MessageEditData.fromCreateData(
                MessageBuilderManager.trackInfo(nowPlaying, music)))
            .and(event.getInteraction().editButton(
                ButtonManager.pause().withStyle(ButtonStyle.PRIMARY).withLabel("Pause ⏸"))).queue();
      } else {
        String message = "The player is already paused!";
        event.replyEmbeds(EmbedManager.default01(message)).setEphemeral(true).queue();
      }
    } else {
      music.pause();
      event.getMessage().editMessage(MessageEditData.fromCreateData(
              MessageBuilderManager.trackInfo(nowPlaying, music)))
          .and(event.getInteraction().editButton(
              ButtonManager.pause().withStyle(ButtonStyle.SUCCESS).withLabel("Resume ⏯"))).queue();
    }
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
  }
}
