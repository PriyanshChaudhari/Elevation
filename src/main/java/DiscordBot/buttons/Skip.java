package DiscordBot.buttons;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.BotUtilities.MessageBuilderManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

@SuppressWarnings("ConstantConditions")
public class Skip extends Button {

  public Skip(DiscordBot bot) {
    super(bot);
    this.name = "skip";
  }

  @Override
  public void execute(ButtonInteractionEvent event) {
    MusicHandler music = bot.musicListener.getMusic(event, false);
    AudioTrack nowPlaying = music.getQueue().size() > 0 ? music.getQueue().getFirst() : null;
    if (music == null) {
      return;
    }

    music.skipTrack();
    ReplyCallbackAction action = event.reply(":fast_forward: Skipping...");
    if (music.getQueue().size() == 1) {
      String text = "The music queue is now empty!";
      action = action.addEmbeds(EmbedManager.createError("Error!", text));
    }
    event.getMessage().editMessage(MessageEditData.fromCreateData(
        MessageBuilderManager.trackInfo(nowPlaying, music))).and(action).queue();
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
  }
}