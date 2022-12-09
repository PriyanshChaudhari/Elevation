package DiscordBot.BotUtilities;

import DiscordBot.LavaPlayer.MusicHandler;
import DiscordBot.commands.Command;
import DiscordBot.commands.CommandRegistry;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public class EmbedManager {

  public static MessageEmbed default01(String message) {
    return new EmbedBuilder()
        .setColor(EmbedColor.DEFAULT.color)
        .setDescription(message)
        .build();
  }

  public static MessageEmbed default01(String title, String message) {
    return new EmbedBuilder()
        .setTitle(title)
        .setColor(EmbedColor.DEFAULT.color)
        .setDescription(message)
        .build();
  }

  public static MessageEmbed createError(String errorMessage) {
    return new EmbedBuilder()
        .setColor(EmbedColor.ERROR.color)
        .setDescription(errorMessage)
        .build();
  }

  public static MessageEmbed createError(String title, String errorMessage) {
    return new EmbedBuilder()
        .setTitle(title)
        .setColor(EmbedColor.ERROR.color)
        .setDescription(errorMessage)
        .build();
  }

  public static MessageEmbed displayTrack(AudioTrack track, MusicHandler handler) {
    String repeat = (handler.isLoop()) ? "Enabled" : "Disabled";
    String userMention = "<@!" + track.getUserData(String.class) + ">";
    return new EmbedBuilder()
        .setTitle("Now Playing :musical_note:")
        .setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")")
        .addField("Requested by", userMention, true)
        .addField("Volume", "`" + handler.audioPlayer.getVolume() + "%`", true)
        .addField("Repeat", "`" + repeat + "`", true)
        .addField("PlayList", "`" + (handler.queue.size() - 1) + " songs`", true)
        .setColor(EmbedColor.PURPLE.color)
        .setThumbnail(MusicHandler.getThumbnail(track))
        .build();
  }

  public static MessageEmbed welcome(GuildMemberJoinEvent event) {
    String user = event.getUser().getAsMention();
    if (event.getUser().getEffectiveAvatarUrl().isEmpty()) {
      return new EmbedBuilder()
          .setTitle("#welcome ")
          .setDescription("Hello " + user)
          .appendDescription("\nWelcome to Blue Ocean...")
          .setThumbnail("F:\\Discord\\Elevation\\src\\main\\resources\\7954692_Sq.png")
          .setColor(EmbedColor.GREEN.color)
          .setFooter(event.getMember().getTimeJoined()
              .format(DateTimeFormatter.ofPattern(" dd.MM.yy | hh.mm a")
                  .withZone(ZoneId.of("Asia/Kolkata"))))
          .build();
    }
    return new EmbedBuilder()
        .setTitle("#welcome ")
        .setDescription("Hello " + user)
        .appendDescription("\nWelcome to Blue Ocean...")
        .setThumbnail(event.getUser().getEffectiveAvatarUrl())
        .setColor(EmbedColor.GREEN.color)
        .setFooter(event.getMember().getTimeJoined()
            .format(DateTimeFormatter.ofPattern(" dd.MM.yy | hh.mm a")
                .withZone(ZoneId.of("Asia/Kolkata"))))
        .build();
  }

  public static MessageEmbed helpList(){
    StringBuilder builder = new StringBuilder();

    //CommandRegistry.commands.stream().map((cmdname, cmddesc)->(String cmdname1 = cmdname.name;String cmddesc1 = cmdname.description))
      //  .forEach((it)-> builder.append('`').append(it).append('`').append(": ").append("\n"));

    CommandRegistry.commandsMap.forEach((cmdname, cmd)->{
      builder.append("`").append(cmdname).append("`").append(": "+cmd.description).append("\n");
    });


    return new EmbedBuilder()
        .setTitle("Elevation Commands")
        .setDescription(builder)
        .setColor(EmbedColor.DEFAULT.color)
        .build();
  }
}