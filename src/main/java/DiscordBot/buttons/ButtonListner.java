package DiscordBot.buttons;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonListner extends ListenerAdapter {

  /*public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    String buttonID = event.getButton().getId();
    if (buttonID.equals("vol-up")) {
      MusicListener music = bot.musicListener;
      music.getMusic(event, true).setVolume((music.getMusic(event, true).audioPlayer.getVolume())+10);
      String message = String.format(":control_knobs: Set the volume to `%s%%`",(music.getMusic(event, true).audioPlayer.getVolume()));
      event.replyEmbeds(EmbedManager.default01(message)).queue();
    }
    if (buttonID.equals("vol-down")) {
      MusicListener music = bot.musicListener;
      music.getMusic(event, true).setVolume((music.getMusic(event, true).audioPlayer.getVolume())-10);
      String message = String.format(":control_knobs: Set the volume to `%s%%`",(music.getMusic(event, true).audioPlayer.getVolume()));
      event.replyEmbeds(EmbedManager.default01(message)).queue();
    }
  }*/
}