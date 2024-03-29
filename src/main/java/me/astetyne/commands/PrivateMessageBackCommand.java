package me.astetyne.commands;

import me.astetyne.ChatPrefabrics;
import me.astetyne.Main;
import me.astetyne.playerdata.GPlayer;
import me.astetyne.privatemessage.PrivateMessageBack;
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
		GPlayer pd = Main.getPlayerData(pp);
		
		if(pd.isMuted()) {
			pp.sendMessage(ChatPrefabrics.SILENCE_CANT_TALK);
			return;
		}
		
		int tokenCount = args.length;
		
		if(tokenCount==0) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/r <me.astetyne.message>"));
		}else if(tokenCount>0) {
			PrivateMessageBack pm = new PrivateMessageBack(pd, args);
			pm.wantsToSendMessage();
		}	
	}
}
