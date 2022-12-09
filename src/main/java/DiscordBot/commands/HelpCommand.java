package DiscordBot.commands;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.DiscordBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class HelpCommand extends Command{

  public HelpCommand(DiscordBot bot) {
    super(bot);
    this.name = "help";
    this.description = "Tells about commands in detail";
    this.args.add(
        new OptionData(
        OptionType.STRING, "command", "name of command to get help", false));
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) throws NullPointerException {

    if (event.getOption("command") == null){
      event.replyEmbeds(EmbedManager.helpList()).setEphemeral(true).queue();
      return;
    }
      String args = event.getOption("command").getAsString();
      Command command = CommandRegistry.commandsMap.get(args);
      if (command == null) {
        String message = ("Nothing found for " + args);
        event.replyEmbeds(EmbedManager.default01(message)).queue();
      }

      String message = ("`%s`: " + command.description).formatted(command.name);
      event.replyEmbeds(EmbedManager.default01(message)).setEphemeral(true).queue();
  }
}