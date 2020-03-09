package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import main.commands.AdminChatCommand;
import main.commands.EventInvitationCommand;
import main.commands.IgnoreCommand;
import main.commands.KickCommand;
import main.commands.MuteAllCommand;
import main.commands.MuteCommand;
import main.commands.NickCommand;
import main.commands.PrivateMessageBackCommand;
import main.commands.PrivateMessageCommand;
import main.commands.SetcolorCommand;
import main.commands.SocialSpyCommand;
import main.commands.UnmuteCommand;
import main.config.GlobalChatConfiguration;
import main.eventinvitation.EventJoinCommand;
import main.message.GlobalMessage;
import main.playerdata.LoaderRunnable;
import main.playerdata.PlayerData;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Main extends Plugin implements Listener {
	
	public static final Logger LOG = BungeeCord.getInstance().getLogger();
	public static List<PlayerData> playerDataList = new ArrayList<PlayerData>();
	public static GlobalChatConfiguration config;
	public static Plugin plugin;
	
	@Override
    public void onEnable() {
		
		plugin = this;
		
		if(getProxy().getPluginManager().getPlugin("LuckPerms")==null) {
			LOG.warning("-----------------------------");
			LOG.warning("GlobalChat requires LuckPerms installed.");
			LOG.warning("Plugin will NOT WORK please install LuckPerms and restart the server.");
			LOG.warning("-----------------------------");
			return;
		}
		
		
        BungeeCord.getInstance().getScheduler().schedule(this, new LoaderRunnable(), 5, 5,TimeUnit.SECONDS);
        
        BungeeCord.getInstance().registerChannel("globalchat");
        
        getProxy().getPluginManager().registerListener(this, this); 
        
        getProxy().getPluginManager().registerCommand(this, new AdminChatCommand());
        getProxy().getPluginManager().registerCommand(this, new KickCommand());
        getProxy().getPluginManager().registerCommand(this, new MuteCommand());
        getProxy().getPluginManager().registerCommand(this, new MuteAllCommand());
        getProxy().getPluginManager().registerCommand(this, new PrivateMessageBackCommand());
        getProxy().getPluginManager().registerCommand(this, new PrivateMessageCommand());
        getProxy().getPluginManager().registerCommand(this, new SetcolorCommand());
        getProxy().getPluginManager().registerCommand(this, new SocialSpyCommand());
        getProxy().getPluginManager().registerCommand(this, new IgnoreCommand());
        getProxy().getPluginManager().registerCommand(this, new UnmuteCommand());
        getProxy().getPluginManager().registerCommand(this, new EventInvitationCommand());
        getProxy().getPluginManager().registerCommand(this, new EventJoinCommand());
        getProxy().getPluginManager().registerCommand(this, new NickCommand());
        
    }

	@EventHandler
	public void onChat(ChatEvent e) {
		
		if(e.isCancelled()) {
			return;
		}

		if(!(e.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		
		String line = e.getMessage();
		
		if(line.startsWith("/")) {
			return;
		}
		
		ProxiedPlayer pp = (ProxiedPlayer) e.getSender();
		PlayerData pd = getPlayerData(pp);
		
		GlobalMessage gs = new GlobalMessage(pd, line);
		gs.wantsToSendMessage();

		e.setCancelled(true);
	}
	
	public static PlayerData getPlayerData(ProxiedPlayer pp) {
		
		for(PlayerData pd : playerDataList) {
			if(pd.getProxiedPlayer().equals(pp)) {
				return pd;
			}
		}
		return null;
	}

	@EventHandler
	public void onPostJoin(PostLoginEvent e) {
		
		PlayerData pd = new PlayerData(e.getPlayer());
		playerDataList.add(pd);
		
		BungeeCord.getInstance().getScheduler().schedule(this,  new Runnable() {
	        @Override
	        public void run() {
	        	if(!e.getPlayer().isConnected()) {
	        		return;
	        	}
	        	pd.setJoinedServer(true);
	        	pd.syncPrefix();
	        	pd.syncMetaValues();
        		
        		for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
        			p.sendMessage(TextComponent.fromLegacyText(ChatColor.DARK_GRAY+"["+ChatColor.GRAY+"WoF"+ChatColor.DARK_GRAY+"] "+ChatColor.WHITE+e.getPlayer().getName()+ChatColor.GRAY+" sa pripojil/a do hry!"));
        		}

        		PluginDescription pdf = plugin.getDescription();
	        	String version = pdf.getVersion();
        		TextComponent globalChatVersionInfo = new TextComponent();
        		globalChatVersionInfo.addExtra(ChatColor.GRAY+"-----------------------------------------\n");
        		globalChatVersionInfo.addExtra(ChatColor.GREEN+"            Running "+ChatColor.WHITE+"GlobalChat "+ChatColor.GREEN+ChatColor.BOLD+version+"\n");
        		globalChatVersionInfo.addExtra(ChatColor.GRAY+"-----------------------------------------");
        		e.getPlayer().sendMessage(globalChatVersionInfo);
	        }
	    }, 2,TimeUnit.SECONDS);
	}

	@EventHandler
	public void onLeave(PlayerDisconnectEvent e) {

		PlayerData pd = getPlayerData(e.getPlayer());
		pd.saveMetaValues();
		playerDataList.remove(pd);
		
		if(pd.isJoinedServer()) {
			for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
		         p.sendMessage(TextComponent.fromLegacyText(ChatColor.DARK_GRAY+"["+ChatColor.GRAY+"WoF"+ChatColor.DARK_GRAY+"] "+ChatColor.WHITE+e.getPlayer()+ChatColor.GRAY+" sa odpojil/a z hry."));
		    }
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onTabComplete(TabCompleteEvent e) {
		
        String[] args = e.getCursor().split(" ");
        final String checked = (args.length > 0 ? args[args.length - 1] : e.getCursor()).toLowerCase();
        
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
            if (player.getName().toLowerCase().startsWith(checked)) {
                e.getSuggestions().add(player.getName());
            }
        }
		
    }
}
