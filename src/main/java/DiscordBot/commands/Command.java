package DiscordBot.commands;

import DiscordBot.DiscordBot;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public abstract class Command {

  public final DiscordBot bot;
  public String name;
  public String description;
  public final List<OptionData> args;
  public final List<SubcommandData> subCommands;

  public Command(DiscordBot bot) {
    this.bot = bot;
    this.args = new ArrayList<>();
    this.subCommands = new ArrayList<>();
  }

  public abstract void execute(SlashCommandInteractionEvent event);
}
