package DiscordBot.commands;

import DiscordBot.BotUtilities.GuildData;
import DiscordBot.DiscordBot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

public class CommandRegistry extends ListenerAdapter {

  public static final List<Command> commands = new ArrayList<>();

  public static final Map<String, Command> commandsMap = new HashMap<>();

  public CommandRegistry(DiscordBot bot) {
    mapCommand(
        new NowPlayingCommand(bot),
        new PlayCommand(bot),
        new PauseCommand(bot),
        new ResumeCommand(bot),
        new RepeatCommand(bot),
        new QueueCommand(bot),
        new SkipCommand(bot),
        new StopCommand(bot),
        new VolumeCommand(bot),
        new ClearCommand(bot),
        new HelpCommand(bot)
    );
  }

  public static List<CommandData> unpackCommandData() {
    // Register slash PlayCommand
    List<CommandData> commandData = new ArrayList<>();
    for (Command command : commands) {
      SlashCommandData slashCommand = Commands.slash(command.name, command.description)
          .addOptions(command.args);
      commandData.add(slashCommand);
    }
    return commandData;
  }

  private void mapCommand(Command... cmds) {
    for (Command cmd : cmds) {
      commandsMap.put(cmd.name, cmd);
      commands.add(cmd);
    }
  }

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    Command cmd = commandsMap.get(event.getName());
    if (cmd != null) {
      // Run command
      cmd.execute(event);
    }
  }

  @Override
  public void onGuildReady(@NotNull GuildReadyEvent event) {
    GuildData.get(event.getGuild());
    event.getGuild().updateCommands().addCommands(unpackCommandData()).queue(succ -> {
    }, fail -> {
    });
  }
}
