package main.privatemessage;

import main.ChatPrefabrics;
import main.playerdata.GPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PrivateMessageBack {
	
	GPlayer pd;
	String[] args;
	
	public PrivateMessageBack(GPlayer pd, String[] args) {
		this.pd = pd;
		this.args = args;
	}
	
	public void wantsToSendMessage() {
		
		ProxiedPlayer sender = pd.getProxiedPlayer();
		String receiverName = pd.getLastPrivateMsgReceiverName();
		
		if(receiverName==null) {
			sender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.GRAY + "No recent messages."));
			return;
		}
		
		PrivateMessage pm = new PrivateMessage(pd, receiverName, args);
		pm.wantsToSendMessage();
	}
}
