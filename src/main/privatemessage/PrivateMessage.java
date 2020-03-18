package main.privatemessage;

import main.ChatPrefabrics;
import main.Main;
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
		sendMessage(pd, receiverData, message);
	}
	
	private void sendMessage(GPlayer senderData, GPlayer receiverData, String msg) {
		
		senderData.setLastPrivateMsgReceiverName(receiverData.getProxiedPlayer().getName());
		receiverData.setLastPrivateMsgReceiverName(senderData.getProxiedPlayer().getName());
		
		ProxiedPlayer sender = senderData.getProxiedPlayer();
		ProxiedPlayer receiver = receiverData.getProxiedPlayer();
		
		receiver.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PRIVATE_MESSAGE + ChatColor.GRAY + sender.getName() + ChatColor.GOLD + " > "
		+ ChatColor.GRAY + "me" + " > " + ChatColor.WHITE + msg));
		
		sender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PRIVATE_MESSAGE + ChatColor.GRAY + "me" + ChatColor.GOLD + " > "
		+ ChatColor.GRAY + receiver.getName() + " > " + ChatColor.WHITE + msg));
		
		for(GPlayer pd : Main.gPlayers) {
			if(pd.hasSocialSpy()) {
				if(!pd.equals(senderData)&&!pd.equals(receiverData)) {
					pd.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SOCIAL_SPY + ChatColor.GRAY + sender.getName() + ChatColor.GOLD + " > "
				+ ChatColor.GRAY + receiver.getName() + " > " + ChatColor.GRAY + ChatColor.ITALIC + msg));
				}
			}
		}
		
		Main.logMessage("[PM] " +sender.getName()+">"+receiver.getName()+">>>"+ msg);
	}
}
