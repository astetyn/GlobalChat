package main.commands;

import main.ChatPrefabrics;
import main.Main;
import main.playerdata.MetaLoader;
import main.playerdata.PlayerData;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SetcolorCommand extends Command {
	
	public SetcolorCommand() {
		super("setcolor","globalchat.setcolor");
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
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.WARNING + ChatColor.RED + "/setcolor <index>"));
		}else if(tokenCount==1) {

			char index = args[0].charAt(0);
			char[] indices = {'r','0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			
			boolean b = false;
			for(char c : indices) {
				if(index == c) {
					b = true;
				}
			}
			if(!b||args[0].length()>1) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.COLOR + ChatColor.RED + "This color is not supported."));
				return;
			}
			
			pd.setColorIndex(index);
			MetaLoader ml = new MetaLoader(pp);
			ml.saveMeta("globalchat-color", index+"");
			
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.COLOR + ChatColor.GREEN + "Color successfuly changed to: "
			+ChatColor.getByChar(index)+"this."));
			
			Main.LOG.info("User: "+pp.getName()+" changed chat color code to: "+index);
			
		}else if(tokenCount>1) {
	        	
			if(!pp.hasPermission("globalchat.setcolor.others")) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.INSUFFICIENT_PERMISSION));
				return;
			}

			char index = args[0].charAt(0);
			char[] indices = {'r','0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			
			boolean b = false;
			for(char c : indices) {
				if(index == c) {
					b = true;
				}
			}
			if(!b||args[0].length()>1) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.COLOR + ChatColor.RED + "This color is not supported."));
				return;
			}
			
			String name = args[1];
			if(ProxyServer.getInstance().getPlayer(name)==null) {
				pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.PLAYER_NOT_ONLINE));
				return;
			}
			
			for(PlayerData pdd : Main.playerDataList) {
				if(pdd.getProxiedPlayer().equals(BungeeCord.getInstance().getPlayer(name))) {
					pdd.setColorIndex(index);
					MetaLoader ml = new MetaLoader(pdd.getProxiedPlayer());
					ml.saveMeta("globalchat-color", index+"");
					pdd.getProxiedPlayer().sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.COLOR + ChatColor.GREEN + "Color successfuly changed to: "
							+ChatColor.getByChar(index)+"this " + ChatColor.GREEN + "from player "+ChatColor.YELLOW+pp.getName()));
					break;
				}
			}
			pp.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.COLOR + ChatColor.GREEN + "Color successfuly changed to: "
			+ChatColor.getByChar(index)+"this " + ChatColor.GREEN + "for player "+ChatColor.YELLOW+name));
			
			Main.LOG.info("User: "+pp.getName()+" changed chat color code to: "+index + " for user: "+name);
			
		}
		
	}
	
}
