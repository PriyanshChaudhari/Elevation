package DiscordBot.BotUtilities;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ButtonManager {
  public static Button volumeUp(){return Button.primary("vol-up", "Increase 🔊");
  }
  public static Button volumeDown(){
    return Button.primary("vol-down", "Decrease 🔉");
  }
  public static Button repeat(){
    return Button.primary("repeat", "Repeat 🔄");
  }
  public static Button skip(){
    return Button.primary("skip", "Skip ⏩");
  }
  public static Button pause(){
    return Button.primary("pause", "Pause ⏸");
  }
  public static Button joinVC(SlashCommandInteractionEvent event){
    return Button.link(event.getGuild().getVoiceChannels().get(1).getJumpUrl(), "Go to Voice Channel");
  }
  public static Button joinVC(ButtonInteractionEvent event){
    return Button.link(event.getGuild().getVoiceChannels().get(1).getJumpUrl(), "Go to Voice Channel");
  }
}