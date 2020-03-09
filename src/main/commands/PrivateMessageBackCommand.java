package main.commands;

import main.ChatPrefabrics;
import main.Main;
import main.playerdata.PlayerData;
import main.privatemessage.PrivateMessageBack;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PrivateMessageBackCommand extends Command {

	public PrivateMessageBackCommand() {
		super("r",null,"reply","m");
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		
		if(!(commandSender instanceof ProxiedPlayer)) {
			commandSender.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.EXECUTE_PLAYER_ONLY));
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) commandSender;
		PlayerData pd = Main.getPlayerData(pp);
		
		if(pd.isMuted()) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE + ChatColor.GRAY + "You are silenced. Your message will not be shown."));
			return;
		}
		
		int tokenCount = args.length;
		
		if(tokenCount==0) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/r <message>"));
		}else if(tokenCount>0) {
			PrivateMessageBack pm = new PrivateMessageBack(pd, args);
			pm.wantsToSendMessage();
		}	
	}
}
