package DiscordBot.BotUtilities;

import DiscordBot.LavaPlayer.MusicHandler;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class GuildData {

  private static final Map<Long, GuildData> guilds = new HashMap<>();
  public MusicHandler musicHandler;

  private GuildData(Guild guild) {
    // Setup caches
    musicHandler = null;

  }


  public static GuildData get(@NotNull Guild guild) {
    if (!guilds.containsKey(guild.getIdLong())) {
      return create(guild);
    }
    return guilds.get(guild.getIdLong());
  }

  private static GuildData create(@NotNull Guild guild) {
    GuildData data = new GuildData(guild);
    guilds.put(guild.getIdLong(), data);
    return data;
  }

}
