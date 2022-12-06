package DiscordBot.BotUtilities;

public enum EmbedColor {
  DEFAULT(0x57a2ff),
  PURPLE(0Xc21cff),
  GREEN(0X34fc2d),
  ERROR(0Xf74939);
  public final int color;
  EmbedColor(int hexCode) {
    this.color = hexCode;
  }
}
