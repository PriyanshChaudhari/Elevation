package DiscordBot.BotUtilities;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Greeting extends ListenerAdapter {

  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    String welcomeID = event.getGuild().getTextChannelsByName("welcome", true).get(0).getId();
    event.getGuild().getTextChannelById(welcomeID).sendMessageEmbeds(EmbedManager.welcome(event)).queue();
  }

}