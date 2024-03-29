package me.astetyne.commands;

import me.astetyne.ChatPrefabrics;
import me.astetyne.Main;
import me.astetyne.Permissions;
import me.astetyne.playerdata.LuckPermsManager;
import me.astetyne.playerdata.GPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class NickCommand extends Command {

	public NickCommand() {
		super("nickname", Permissions.CHANGE_NICK, "nick", "changenick","customnick");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		GPlayer pd = Main.getPlayerData(pp);
		
		int tokenCount = args.length;
		
		if(tokenCount==0) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/nick <newNickname> [playersName]"));
		}else if(tokenCount==1) {
			
			String nickname = args[0];
			if(nickname.equals("delete")) {
				pd.setCustomNick(null);
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.NICKNAME + ChatColor.GRAY + "Nick deleted successfuly."));
			}else {
				pd.setCustomNick(nickname);
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.NICKNAME + ChatColor.GRAY + "Nick is now active."));
			}
		}else {
			
			String nickname = args[0];
			String playerName = args[1];
			
			if(ProxyServer.getInstance().getPlayer(playerName)==null) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PLAYER_NOT_ONLINE));
				return;
			}
			
			ProxiedPlayer pp2 = ProxyServer.getInstance().getPlayer(playerName);
			GPlayer pd2 = Main.getPlayerData(pp2);
			
			if(nickname.equals("delete")) {
				pd2.setCustomNick(null);
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.NICKNAME + ChatColor.GRAY + "Nick deleted successfuly for player: "+playerName));
				pp2.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.NICKNAME + ChatColor.GRAY + "Nick deleted successfuly from player: "+playerName));
				LuckPermsManager.removeMetaNode(pp2, "globalchat-nick");
			}else {
				pd2.setCustomNick(nickname);
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.NICKNAME + ChatColor.GRAY + "Nick changed successfuly for player: "+playerName));
				pp2.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.NICKNAME + ChatColor.GRAY + "Nick changed successfuly from player: "+playerName));
			}
		}
		
	}

}
