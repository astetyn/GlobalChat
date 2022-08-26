package me.astetyne.commands;

import me.astetyne.ChatPrefabrics;
import me.astetyne.Main;
import me.astetyne.Permissions;
import me.astetyne.playerdata.GPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AdminChatCommand extends Command {

	public AdminChatCommand() {
		super("ac", Permissions.ADMINCHAT, "adminchat", "a");
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
			
			String customNickname = pd.getCustomNick();
			String nickname = (customNickname == null) ? pp.getName() : customNickname;
			
			if(pd.getProxiedPlayer().hasPermission(Permissions.COLORED_MESSAGES)) {
				message = ChatColor.translateAlternateColorCodes('&', message);
			}
			
			message = message.trim();
			if(message.isEmpty()) {
				return;
			}
			
			for(GPlayer pdd : Main.gPlayers) {
				ProxiedPlayer admin = pdd.getProxiedPlayer();
				if(pdd.getProxiedPlayer().hasPermission(Permissions.ADMINCHAT)) {
					admin.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.ADMIN_CHAT + ChatColor.GRAY + nickname + "> " + ChatColor.RED + message));
				}
			}
			Main.logMessage("[AC] "+pp.getName()+">"+message);
		}
		
	}
}
