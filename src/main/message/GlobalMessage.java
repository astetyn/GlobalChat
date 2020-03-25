package main.message;

import java.util.List;

import main.ChatPrefabrics;
import main.Main;
import main.Permissions;
import main.config.GlobalChatConfiguration;
import main.playerdata.GPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class GlobalMessage {
	
	private ProxiedPlayer sender;
	private GPlayer pd;
	private String message;
	private String prefix;
	private String suffix;
	private Server server;
	private String nickname;
	
	public GlobalMessage(GPlayer pd, String message) {
		this.sender = pd.getProxiedPlayer();
		this.message = message;
		this.pd = pd;
		this.prefix = pd.getPrefix();
		this.suffix = pd.getSuffix();
		this.server = sender.getServer();
		String customNickname = pd.getCustomNick();
		this.nickname = (customNickname == null) ? pd.getProxiedPlayer().getName() : customNickname;
	}
	
	public void wantsToSendMessage() {
		
		if(pd.isMuted()) {
			sender.sendMessage(ChatPrefabrics.SILENCE_CANT_TALK);
			return;
		}
		
		if(pd.hasActivedAdminChat()) {
			for(GPlayer pdd : Main.gPlayers) {
				if(pdd.getProxiedPlayer().hasPermission(Permissions.ADMINCHAT)) {
					ProxiedPlayer admin = pdd.getProxiedPlayer();
					if(admin.hasPermission(Permissions.COLORED_MESSAGES)) {
						message = ChatColor.translateAlternateColorCodes('&', message);
					}
					admin.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.ADMIN_CHAT + ChatColor.GRAY + nickname + "> " + ChatColor.RED + message));
				}
			}
			Main.logMessage("[AC] "+pd.getProxiedPlayer().getName()+">"+message);
			return;
		}
		
		if(!pd.getProxiedPlayer().hasPermission(Permissions.NO_MESSAGE_CHECK)) {
			if(isMessageSpam(message)) {
				return;
			}
			message = createCleanMessage(message);
			message = message.trim();
			if(message.isEmpty()) {
				return;
			}
		}
		message = message.trim();
		char msgColorIndex = pd.getColorIndex();
		message = ChatColor.getByChar(msgColorIndex) + message;
		
		if(pd.getProxiedPlayer().hasPermission(Permissions.COLORED_MESSAGES)) {
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		
		createAndSendFinalMessage();
	}
	
	private void createAndSendFinalMessage() {
		
		pd.setLastMessage(message);
		pd.setLastTimestamp(System.currentTimeMillis());
		
		String finalMsg = "";
		
		if(GlobalChatConfiguration.config.getBoolean("showServerIcon")) {
			finalMsg += getServerIcon(server);
		}
		
		finalMsg += ChatColor.translateAlternateColorCodes('&', prefix);
		
		if(GlobalChatConfiguration.config.getBoolean("addExtraSpace")) {
			finalMsg += " ";
		}
		
		if(GlobalChatConfiguration.config.getBoolean("separateColors")) {
			finalMsg += ChatColor.RESET;
		}
		
		if(prefix.isEmpty()||GlobalChatConfiguration.config.getBoolean("separateColors")) {
			char c = GlobalChatConfiguration.config.getString("defaultNicknameColor").charAt(0);
			ChatColor defaultNicknameColor = ChatColor.getByChar(c);
			nickname = defaultNicknameColor + nickname;
		}
		
		finalMsg += ChatColor.translateAlternateColorCodes('&', nickname);
		
		if(GlobalChatConfiguration.config.getBoolean("addExtraSpace")) {
			finalMsg += " ";
		}
		finalMsg += ChatColor.RESET;
		finalMsg += ChatColor.translateAlternateColorCodes('&', suffix);
		
		char c = GlobalChatConfiguration.config.getString("defaultArrowColor").charAt(0);
		
		finalMsg += ChatColor.getByChar(c)+""+ChatColor.BOLD+" > ";
		finalMsg += message;
		
		for(GPlayer pdd : Main.gPlayers) {
			
			ProxiedPlayer pp = pdd.getProxiedPlayer();
			
			boolean ignored = false;
			
			for(String ignoreName : pdd.getIgnoredPlayersNames()) {
				if(ignoreName.equals(sender.getName())) {
					ignored = true;
				}
			}
			if(ignored) {
				continue;
			}
			
			pp.sendMessage(TextComponent.fromLegacyText(finalMsg));
		}
		Main.logMessage(sender.getName()+ ">"+ message);
	}
	
	private boolean isMessageSpam(String msg) {
		
		int waitTime = GlobalChatConfiguration.config.getInt("waitSpamTime");
		
		if(pd.getLastTimestamp()+waitTime*1000>System.currentTimeMillis()) {
			sender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SPAM + ChatColor.RED + "Calm down! Wait few seconds before next message."));
			return true;
		}
		
		if(ChatColor.stripColor(pd.getLastMessage()).equalsIgnoreCase(ChatColor.stripColor(message))) {
			sender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SPAM + ChatColor.RED + "Do not send similar messages!"));
			return true;
		}
		return false;
		
	}
	
	private String createCleanMessage(String msg) {
		
		msg = msg.replace('.', ' ');
		
		while(msg.contains("  ")) {
			msg = msg.replaceAll("  ", " ");
		}
		
		msg = stripBadWords(msg);
		
		return msg;
		
	}

	private String getServerIcon(Server server) {
		
		char firstChar;
		firstChar = server.getInfo().getName().charAt(0);
		return (ChatColor.GRAY+"["+ChatColor.YELLOW+firstChar+ChatColor.GRAY+"] "+ChatColor.RESET);
		
	}

	private String stripBadWords(String msg) {
		
		List<String> badWords = GlobalChatConfiguration.config.getStringList("badWords");
		
		for(String badWord : badWords) {
			if(msg.contains(badWord)) {
				msg = msg.replaceAll(badWord, "***");
			}
		}
		return msg;
	}
	
	
}
