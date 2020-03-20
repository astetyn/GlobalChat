package main.privatemessage;

import main.ChatPrefabrics;
import main.Main;
import main.Permissions;
import main.playerdata.GPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PrivateMessage {
	
	GPlayer pd;
	String receiverName;
	String message;
	
	public PrivateMessage(GPlayer pd, String receiver, String[] args) {
		this.pd = pd;
		this.receiverName = receiver;
		String msg = "";
		for(int i = 0;i<args.length;i++) {
			msg += args[i]+" ";
		}
		this.message = msg;
	}
	
	public void wantsToSendMessage() {
		
		ProxiedPlayer sender = pd.getProxiedPlayer();
		
		if(ProxyServer.getInstance().getPlayer(receiverName)==null) {
			sender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PLAYER_NOT_ONLINE));
			return;
		}
		
		ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(receiverName);
		GPlayer receiverData = Main.getPlayerData(receiver);
		
		for(String ignoreName : receiverData.getIgnoredPlayersNames()) {
			if(ignoreName.equals(sender.getName())) {
				sender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + "Receiver has blocked your messages."));
				return;
			}
		}
		sendMessage(pd, receiverData);
	}
	
	private void sendMessage(GPlayer gSender, GPlayer gReceiver) {
		
		gSender.setLastPrivateMsgReceiverName(gReceiver.getProxiedPlayer().getName());
		gReceiver.setLastPrivateMsgReceiverName(gSender.getProxiedPlayer().getName());
		
		ProxiedPlayer sender = gSender.getProxiedPlayer();
		ProxiedPlayer receiver = gReceiver.getProxiedPlayer();
		
		String sName = sender.getName();
		String rName = receiver.getName();
		
		if(gSender.getCustomNick()!=null) {
			sName = gSender.getCustomNick();
		}
		if(gReceiver.getCustomNick()!=null) {
			rName = gReceiver.getCustomNick();
		}
		
		if(gSender.getProxiedPlayer().hasPermission(Permissions.COLORED_MESSAGES)) {
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		
		receiver.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PRIVATE_MESSAGE+ChatColor.GRAY+sName+ChatColor.GOLD+" > "
		+ ChatColor.GRAY + "me" + " > " + ChatColor.WHITE + message));
		
		sender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PRIVATE_MESSAGE + ChatColor.GRAY + "me" + ChatColor.GOLD + " > "
		+ ChatColor.GRAY + rName + " > " + ChatColor.WHITE + message));
		
		for(GPlayer pd : Main.gPlayers) {
			if(pd.hasSocialSpy()) {
				if(!pd.equals(gSender)&&!pd.equals(gReceiver)) {
					pd.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SOCIAL_SPY + ChatColor.GRAY + sName + ChatColor.GOLD + " > "
				+ ChatColor.GRAY + rName + " > " + ChatColor.GRAY + ChatColor.ITALIC + message));
				}
			}
		}
		
		Main.logMessage("[PM] " +sender.getName()+">"+receiver.getName()+">>>"+ message);
	}
}
