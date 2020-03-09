package main.message;

import java.util.Arrays;
import java.util.List;

import main.ChatPrefabrics;
import main.Main;
import main.playerdata.PlayerData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class GlobalMessage {
	
	private final double MESSAGE_DELAY_SECONDS = 2;
	
	private ProxiedPlayer sender;
	private PlayerData pd;
	private String message;
	private String prefix;
	private Server server;
	private String nickname;
	
	public GlobalMessage(PlayerData pd, String message) {
		this.sender = pd.getProxiedPlayer();
		this.message = message;
		this.pd = pd;
		this.prefix = pd.getPrefix();
		this.server = sender.getServer();
		String customNickname = pd.getCustomNick();
		this.nickname = (customNickname == null) ? pd.getProxiedPlayer().getName() : customNickname;
	}
	
	public void wantsToSendMessage() {
		
		if(pd.isMuted()) {
			sender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + ChatColor.GRAY + "You are silenced. Your message will not be shown."));
			return;
		}
		
		if(pd.hasActivedAdminChat()) {
			for(PlayerData pdd : Main.playerDataList) {
				if(pdd.getProxiedPlayer().hasPermission("globalchat.adminchat")) {
					ProxiedPlayer admin = pdd.getProxiedPlayer();
					message = ChatColor.translateAlternateColorCodes('&', message);
					admin.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.ADMIN_CHAT + ChatColor.GRAY + nickname + "> " + ChatColor.RED + message));
				}
			}
			return;
		}
		
		if(!pd.getProxiedPlayer().hasPermission("globalchat.adminchat")) {
			if(isMessageSpam(message)) {
				return;
			}
			message = createCleanMessage(message);
		}
		
		char msgColorIndex = pd.getColorIndex();
		message = ChatColor.getByChar(msgColorIndex) + message;
		
		if(pd.getProxiedPlayer().hasPermission("globalchat.adminchat")) {
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		
		createAndSendFinalMessage();
	}
	
	private void createAndSendFinalMessage() {
		
		pd.setLastMessage(message);
		pd.setLastTimestamp(System.currentTimeMillis());
		
		if(prefix.isEmpty()) {
			prefix = ChatColor.DARK_GRAY+"";
		}
		
		String finalMsg = "";
		
		finalMsg += getServerString(server);
		finalMsg += ChatColor.translateAlternateColorCodes('&', prefix);
		finalMsg += nickname;
		finalMsg += ChatColor.GRAY+""+ChatColor.BOLD+" > ";
		finalMsg += message;
	
		Main.LOG.info(sender.getName()+ " > "+ message);
		
		for(PlayerData pdd : Main.playerDataList) {
			
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
	}
	
	private boolean isMessageSpam(String msg) {
		
		if(pd.getLastTimestamp()+MESSAGE_DELAY_SECONDS*1000>System.currentTimeMillis()) {
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

	private String getServerString(Server server) {
		
		char firstChar;
		firstChar = server.getInfo().getName().charAt(0);
		return (ChatColor.GRAY+"["+ChatColor.YELLOW+firstChar+ChatColor.GRAY+"] "+ChatColor.RESET);
		
	}

	private String stripBadWords(String sprava) {
		
		List<String> badWords = Arrays.asList("dick","penis","vagina","fuck","kurva","kurwa","pica","kokot",
				"jebat","jebe","pice","idiot","debil","picus","imbecil","retard","bitch");
		
		for(String badWord : badWords) {
			if(sprava.contains(badWord)) {
				sprava = sprava.replaceAll(badWord, "***");
			}
		}
		return sprava;
	}
}
