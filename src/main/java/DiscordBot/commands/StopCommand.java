package DiscordBot.commands;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.BotUtilities.GuildData;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SuppressWarnings("ConstantConditions")
public class StopCommand extends Command {

  public StopCommand(DiscordBot bot) {
    super(bot);
    this.name = "stop";
    this.description = "Stop the current song and clear the entire music queue.";
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    MusicHandler musicHandler = GuildData.get(event.getGuild()).musicHandler;
    if (musicHandler == null) {
      String message = "The music player is already stopped!";
      event.replyEmbeds(EmbedManager.default01(message)).setEphemeral(true).queue();
    } else {
      musicHandler.disconnect();
      event.getGuild().getAudioManager().closeAudioConnection();
      String message = ":stop_button: Stopped the music player.";
      event.replyEmbeds(EmbedManager.default01(message)).queue();
    }
  }
}
