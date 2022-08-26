package me.astetyne.eventinvitation;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class EventInvitation {
	
	private static ProxiedPlayer eventCreator;
	private static Server targetServer;
	private static String eventName;
	private static String cmd;
	
	public EventInvitation(ProxiedPlayer eventCreator, Server targetServer, String eventName, String[] cmdArgs) {
		
		EventInvitation.eventCreator = eventCreator;
		EventInvitation.targetServer = targetServer;
		EventInvitation.eventName = eventName;
		String finalCmd = "/";
		for(int i = 0; i<cmdArgs.length;i++) {
			finalCmd+=cmdArgs[i]+" ";
		}
		EventInvitation.cmd = finalCmd;
		sendInvitation();
		
	}
	
	private void sendInvitation() {
		String invitationMsgStr = "";
		invitationMsgStr += ChatColor.DARK_GREEN+""+ChatColor.BOLD+"---------------["+ChatColor.GREEN+ChatColor.BOLD+
				" E V E N T "+ChatColor.DARK_GREEN+ChatColor.BOLD+"]---------------\n\n";
		invitationMsgStr += ChatColor.GRAY+"Event starting: "+ChatColor.YELLOW+ChatColor.BOLD+eventName+ChatColor.GRAY+" od "+ChatColor.WHITE+eventCreator+"\n";
		invitationMsgStr += ChatColor.GRAY+"For join: "+ChatColor.DARK_GREEN+ChatColor.BOLD+">> "+ChatColor.WHITE+ChatColor.BOLD+"CLICK HERE"
		+ChatColor.DARK_GREEN+ChatColor.BOLD+" <<\n\n";
		invitationMsgStr += ChatColor.DARK_GREEN+""+ChatColor.BOLD+"---------------["+ChatColor.GREEN+ChatColor.BOLD+
				" E V E N T "+ChatColor.DARK_GREEN+ChatColor.BOLD+"]---------------";
		
		TextComponent invitationMsg = new TextComponent(TextComponent.toLegacyText(TextComponent.fromLegacyText(invitationMsgStr)));
		
		invitationMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/eventjoin"));
		invitationMsg.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("For joining to this event click on this me.astetyne.message.")));
		for(ProxiedPlayer pp : ProxyServer.getInstance().getPlayers()) {
			pp.sendMessage(invitationMsg);
		}
	}
	
	public static void onEventJoin(ProxiedPlayer joiner) {
		if(eventCreator==null) {
			return;
		}
		new ServerConnector(joiner, targetServer, cmd);
	}
	
}
