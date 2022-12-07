package DiscordBot;

import DiscordBot.BotUtilities.Greeting;
import DiscordBot.LavaPlayer.MusicListener;
import DiscordBot.buttons.ButtonRegistry;
import DiscordBot.commands.CommandRegistry;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordBot {

  public final ShardManager shardManager;
  public MusicListener musicListener = new MusicListener("6a71811453f04eff8fdb80827eb54147",
      "54a7f88a52f9466a870b24e4f2c7031e");


  //DiscordBot Constructor
  public DiscordBot() {
    Dotenv config = Dotenv.configure().ignoreIfMissing().load();
    DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(
        config.get("TOKEN", System.getenv("TOKEN")));

    builder.setStatus(OnlineStatus.ONLINE)
        .setActivity(Activity.watching("Server Blue Ocean"))
        .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_PRESENCES)
        .addEventListeners(new Greeting(), new CommandRegistry(this), new ButtonRegistry(this))
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .setChunkingFilter(ChunkingFilter.ALL)
        .enableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.VOICE_STATE);
    shardManager = builder.build();

    //
    MusicListener musicListener = new MusicListener(config.get("SPOTIFY_CLIENT_ID"),
        config.get("SPOTIFY_TOKEN"));
    shardManager.addEventListener(
        musicListener);

  }

  //Main method
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    DiscordBot bot = new DiscordBot();
  }
}
