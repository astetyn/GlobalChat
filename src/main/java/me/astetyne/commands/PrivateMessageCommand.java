package me.astetyne.commands;

import me.astetyne.ChatPrefabrics;
import me.astetyne.Main;
import me.astetyne.playerdata.GPlayer;
import me.astetyne.privatemessage.PrivateMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PrivateMessageCommand extends Command {

	public PrivateMessageCommand() {
		super("msg", null, "message","tell","w");
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
		
		if(tokenCount<2) {
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PRIVATE_MESSAGE + ChatColor.RED + "/msg <receiver> <me.astetyne.message>"));
		}else if(tokenCount>=2) {
			String receiverName = args[0];
			String[] newArgs = new String[args.length-1];
			for(int i = 1;i<args.length;i++) {
				newArgs[i-1] = args[i];
			}
			PrivateMessage pm = new PrivateMessage(pd, receiverName, newArgs);
			pm.wantsToSendMessage();
		}
		
	}

}
