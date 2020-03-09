package main.commands;

import main.ChatPrefabrics;
import main.Main;
import main.playerdata.PlayerData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AdminChatCommand extends Command {

	public AdminChatCommand() {
		super("ac", "globalchat.adminchat", "adminchat", "a");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		PlayerData pd = Main.getPlayerData(pp);
		
		int tokenCount = args.length;
		
		if(tokenCount==0) {
			if(pd.hasActivedAdminChat()) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.ADMIN_CHAT + ChatColor.RED + "deactivated."));
				pd.setActivedAdminChat(false);
			}else {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.ADMIN_CHAT + ChatColor.GREEN + "activated."));
				pd.setActivedAdminChat(true);
			}
			return;
		}else if(tokenCount>0) {
			String message = "";
			for(int i = 0;i<tokenCount;i++) {
				message += args[i]+" ";
			}
			for(PlayerData pdd : Main.playerDataList) {
				ProxiedPlayer admin = pdd.getProxiedPlayer();
				if(pdd.getProxiedPlayer().hasPermission("globalchat.adminchat")) {
					admin.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.ADMIN_CHAT + ChatColor.GRAY + pp.getName() + "> " + ChatColor.RED + message));
				}
			}
			Main.LOG.info("AC "+pp.getName()+"> "+message);
		}
		
	}
}
