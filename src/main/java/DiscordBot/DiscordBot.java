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
  public MusicListener musicListener;


  //DiscordBot Constructor
  public DiscordBot() {
    Dotenv config = Dotenv.configure().ignoreIfMissing().load();

    System.out.println(config.get("SPOTIFY_CLIENT_ID"));
    if (config.get("SPOTIFY_CLIENT_ID") == null || config.get("SPOTIFY_TOKEN") == null) {
      throw new IllegalArgumentException("Spotify credentials are not set in the environment variables.");
    }

    musicListener = new MusicListener(config.get("SPOTIFY_CLIENT_ID"),
            config.get("SPOTIFY_TOKEN"));

    DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(
        config.get("TOKEN", System.getenv("TOKEN")));

    builder.setStatus(OnlineStatus.ONLINE)
        .setActivity(Activity.watching("Blue Ocean"))
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
