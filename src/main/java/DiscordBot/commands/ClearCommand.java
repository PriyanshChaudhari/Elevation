package DiscordBot.commands;

import DiscordBot.BotUtilities.EmbedManager;
import DiscordBot.DiscordBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@SuppressWarnings("ConstantConditions")
public class ClearCommand extends Command {

  public ClearCommand(DiscordBot bot) {
    super(bot);
    this.name = "clear";
    this.description = "Clear amount of messages in channel.";
    this.args.add(
        new OptionData(OptionType.INTEGER, "amount", "No. of messages to be deleted.", true)
            .setMinValue(1)
            .setMaxValue(100)
    );
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    event.deferReply().setEphemeral(true).queue();
    if (event.getMember().isOwner() || event.getMember().getUser().isBot()) {
      int amount = event.getOption("amount").getAsInt();
      event.getChannel().getHistory().retrievePast(Math.min(amount + 1, 100)).queue(messages -> {
        try {
          // Delete messages and notify user
          (event.getChannel().asGuildMessageChannel()).deleteMessages(messages).queue(result -> {
            String text = ":ballot_box_with_check: Cleared `%d` messages!".formatted(amount);
            event.getHook().sendMessage(text).queue();
          });
        } catch (IllegalArgumentException e) {
          // Messages were older than 2 weeks
          String text = "You cannot clear messages older than 2 weeks!";
          event.getHook().sendMessageEmbeds(EmbedManager.createError(text)).queue();
        }
      });
    } else {
      String text = "Clear command is only for Server Owner. You can't use it.";
      event.getHook().sendMessageEmbeds(EmbedManager.createError(text)).setEphemeral(true).queue();
    }
  }
}