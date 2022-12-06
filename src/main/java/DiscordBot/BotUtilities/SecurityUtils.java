package DiscordBot.BotUtilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SecurityUtils {

  private static final List<String> ALLOWED_PROTOCOLS = List.of("http", "https");
  private static final List<String> ALLOWED_DOMAINS = List.of("youtube", "soundcloud", "twitch", "spotify", "apple");

  public static boolean isUrlWhitelisted(String urlString) throws MalformedURLException {
    URL url = new URL(urlString);
    boolean isValidProtocol = ALLOWED_PROTOCOLS.contains(url.getProtocol());
    String host = url.getHost();
    if(host.equals("youtu.be")) {
      return true;
    }
    String domain = getDomain(host);
    boolean isValidDomain = ALLOWED_DOMAINS.contains(domain);
    return isValidProtocol && isValidDomain;
  }

  public static String getDomain(String host) {
    String[] parts = host.split("(\\.|%2E)"); // Match dot or URL-Encoded dot
    int size = parts.length;
    if(size == 1) {
      return host;
    }
    return parts[size - 2];
  }

}
