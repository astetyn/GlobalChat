package me.astetyne.playerdata;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import me.astetyne.Permissions;
import me.astetyne.commands.MuteCommand;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LuckPermsManager {
	
	private static LuckPerms api = LuckPermsProvider.get();
	
	public static void loadAllDataToPlayer(GPlayer gPlayer) {
		
		User user = api.getUserManager().getUser(gPlayer.getProxiedPlayer().getUniqueId());
		QueryOptions qo = api.getContextManager().getQueryOptions(gPlayer.getProxiedPlayer());
		CachedMetaData metaData = user.getCachedData().getMetaData(qo);
		
		syncPrefixAndSufix(gPlayer);
		
	    if(!gPlayer.getProxiedPlayer().hasPermission(Permissions.SET_COLOR)) {
			removeMetaNode(gPlayer.getProxiedPlayer(),"globalchat-color");
		}
	    if(!gPlayer.getProxiedPlayer().hasPermission(Permissions.SOCIAL_SPY)) {
	    	removeMetaNode(gPlayer.getProxiedPlayer(),"globalchat-socialspy");
	    }
	    
	    if(metaData.getMeta().containsKey("globalchat-color")) {
	    	gPlayer.setColorIndex(metaData.getMetaValue("globalchat-color").charAt(0));
	    }
	    if(metaData.getMeta().containsKey("globalchat-nick")) {
	    	gPlayer.setCustomNick(metaData.getMetaValue("globalchat-nick"));
	    }
	    if(metaData.getMeta().containsKey("globalchat-socialspy")) {
	    	gPlayer.setSocialSpy(Boolean.parseBoolean(metaData.getMetaValue("globalchat-socialspy")));
	    }
	    if(metaData.getMeta().containsKey("globalchat-mute")) {
	    	gPlayer.setMuted(true);
	    }
	    
	}
	
	public static void saveAllDataFromPlayer(GPlayer gPlayer) {
		
		User user = api.getUserManager().getUser(gPlayer.getProxiedPlayer().getUniqueId());
		
		removeMetaNode(gPlayer.getProxiedPlayer(), "globalchat-color");
		removeMetaNode(gPlayer.getProxiedPlayer(), "globalchat-nick");
		removeMetaNode(gPlayer.getProxiedPlayer(), "globalchat-socialspy");
		removeMetaNode(gPlayer.getProxiedPlayer(), "globalchat-mute");
		
		if(gPlayer.getProxiedPlayer().hasPermission(Permissions.SET_COLOR)) {
			if(gPlayer.getColorIndex()!='7') {
				user.data().add(MetaNode.builder("globalchat-color",gPlayer.getColorIndex()+"").build());
			}
		}
		if(gPlayer.getCustomNick()!=null) {
			user.data().add(MetaNode.builder("globalchat-nick",gPlayer.getCustomNick()).build());
		}
		if(gPlayer.hasSocialSpy()) {
			user.data().add(MetaNode.builder("globalchat-socialspy","true").build());
		}
		if(gPlayer.isMuted()) {
			user.data().add(MetaNode.builder("globalchat-mute",gPlayer.isMuted()+"").build());
		}
		api.getUserManager().saveUser(user);
		
	}
	
	public static void removeAllMuteNodes() {
		
		UserManager um = api.getUserManager();
		ContextManager cm = api.getContextManager();
		
		for(UUID uuid : MuteCommand.mutedPlayersUUIDs) {
			
			CompletableFuture<User> userFuture = um.loadUser(uuid);

		    userFuture.thenAcceptAsync(user -> {
		    	QueryOptions qo = cm.getQueryOptions(user).orElse(cm.getStaticQueryOptions());
		    	CachedMetaData metaData = user.getCachedData().getMetaData(qo);
		    	
			    if(!metaData.getMeta().containsKey("globalchat-mute")) {
			    	return;
			    }
			    
		    	for(Node node : user.getNodes()) {
		    		if(!(node instanceof MetaNode)) {
		    			continue;
		    		}
		    		if(((MetaNode)node).getMetaKey().equals("globalchat-mute")) {
		    			user.data().remove(node);
		    			um.saveUser(user);
		    		}
		    	}
		    });
		}
		
	}
	
	public static void syncPrefixAndSufix(GPlayer gPlayer) {

		if(gPlayer.getProxiedPlayer()==null) {
			return;
		}
		
		User user = api.getUserManager().getUser(gPlayer.getProxiedPlayer().getUniqueId());
		if(user==null) {
			return;
		}
		QueryOptions qo = api.getContextManager().getQueryOptions(gPlayer.getProxiedPlayer());
		CachedMetaData metaData = user.getCachedData().getMetaData(qo);
		
		String prefix = metaData.getPrefix();
	    if(prefix!=null) {
	    	gPlayer.setPrefix(prefix);
	    }else {
	    	gPlayer.setPrefix("");
	    }
	    String suffix = metaData.getSuffix();
	    if(suffix!=null) {
	    	gPlayer.setSuffix(suffix);
	    }else {
	    	gPlayer.setSuffix("");
	    }
	}
	
	public static void removeMetaNode(ProxiedPlayer pp, String key) {
		
		User user = api.getUserManager().getUser(pp.getUniqueId());
		QueryOptions qo = api.getContextManager().getQueryOptions(pp);
		CachedMetaData metaData = user.getCachedData().getMetaData(qo);
		
	    if(!metaData.getMeta().containsKey(key)) {
	    	return;
	    }
	    
    	for(Node node : user.getNodes()) {
    		if(!(node instanceof MetaNode)) {
    			continue;
    		}
    		if(((MetaNode)node).getMetaKey().equals(key)) {
    			user.data().remove(node);
    			api.getUserManager().saveUser(user);
    		}
    	}
	}
}
