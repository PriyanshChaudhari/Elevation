package DiscordBot.commands;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.DiscordBot;
import DiscordBot.LavaPlayer.MusicHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ConstantConditions")
public class VolumeCommand extends Command {

  public VolumeCommand(DiscordBot bot) {
    super(bot);
    this.name = "volume";
    this.description = "Changes the volume of the music.";
    this.args.add(new OptionData(OptionType.INTEGER, "amount", "Enter value between 0-100 to set", true)
        .setMinValue(0)
        .setMaxValue(100));
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    int volume = event.getOption("amount").getAsInt();
    MusicHandler music = bot.musicListener.getMusic(event, true);
    if (music == null) return;
    try {
      if (volume < 0 || volume > 100) {
        throw new NumberFormatException();
      }
      music.setVolume(volume);
      String message = String.format(":control_knobs: Set the volume to `%s%%`", volume);
      event.replyEmbeds(EmbedManager.default01(message)).queue();
      return;
    } catch (@NotNull NumberFormatException | ArrayIndexOutOfBoundsException e) {e.printStackTrace();}

    String message = "You must specify a volume between 0-100";
    event.replyEmbeds(EmbedManager.createError(message)).queue();
  }
}
