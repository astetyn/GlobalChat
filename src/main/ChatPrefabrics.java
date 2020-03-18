package main;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatPrefabrics {

	public static final String WARNING = ChatColor.DARK_GRAY+"["+ChatColor.RED+"WARN"+ChatColor.DARK_GRAY+"] "+ChatColor.RESET;
	
	public static final String ADMIN_CHAT = ChatColor.DARK_GRAY+"["+ChatColor.GOLD+"AdminChat"+ChatColor.DARK_GRAY+"] "+ChatColor.RESET;
	
	public static final String SILENCE = ChatColor.DARK_GRAY+"["+ChatColor.GRAY+"SILENCE"+ChatColor.DARK_GRAY+"] "+ChatColor.RESET;
	
	public static final String PRIVATE_MESSAGE = ChatColor.DARK_GRAY+"["+ChatColor.GRAY+"PM"+ChatColor.DARK_GRAY+"] "+ChatColor.RESET;
	
	public static final String COLOR = ChatColor.DARK_GRAY+"["+ChatColor.GRAY+"COLOR"+ChatColor.DARK_GRAY+"] "+ChatColor.RESET;
	
	public static final String SOCIAL_SPY = ChatColor.DARK_GRAY+"["+ChatColor.GRAY+"SS"+ChatColor.DARK_GRAY+"] "+ChatColor.RESET;
	
	public static final String SPAM = ChatColor.DARK_GRAY+"["+ChatColor.RED+"SPAM"+ChatColor.DARK_GRAY+"] "+ChatColor.RESET;
	
	public static final String NICKNAME = ChatColor.DARK_GRAY+"["+ChatColor.GRAY+"NICK"+ChatColor.DARK_GRAY+"] "+ChatColor.RESET;
	
	public static final String INSUFFICIENT_PERMISSION = WARNING+ChatColor.GRAY+"You do not have permission to do this!";
	
	public static final String EXECUTE_PLAYER_ONLY = ChatColor.RED+"This command can be executed only as a player.";
	
	public static final String PLAYER_NOT_ONLINE = WARNING + ChatColor.RED + "Player is not online.";
	
	
	public static final BaseComponent[] SILENCE_CANT_TALK = TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + ChatColor.GRAY + "You are silenced. Your message will not be shown.");
	
}
