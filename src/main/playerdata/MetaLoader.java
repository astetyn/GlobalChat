package main.playerdata;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MetaLoader {

	private LuckPerms api = LuckPermsProvider.get();
	private User user;
	private QueryOptions queryOptions;
	
	public MetaLoader(ProxiedPlayer pp) {
		this.user = api.getUserManager().getUser(pp.getUniqueId());
		this.queryOptions = api.getContextManager().getQueryOptions(pp);
	}
	
	public String loadPrefix() {
		String prefix = user.getCachedData().getMetaData(queryOptions).getPrefix();
	    if(prefix==null) {
	    	prefix = "";
	    }
	    return prefix;
	}
	
	public String loadMeta(String key) {
	    CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);
	    if(metaData.getMeta().containsKey(key)) {
	    	return metaData.getMeta().get(key).get(0);
	    }
		return null;
	}
	
	public void saveMeta(String key, String value) {
        
        removeOwnMeta(key);
        
	    MetaNode node = MetaNode.builder(key,value).build();
	    user.data().add(node);
	    api.getUserManager().saveUser(user);
	}
	
	public void removeOwnMeta(String key) {
		
        CachedDataManager cachedData = user.getCachedData();
	    CachedMetaData metaData = cachedData.getMetaData(queryOptions);
	    
	    if(metaData.getMeta().containsKey(key)) {
	    	for(Node node : user.getNodes()) {
	    		if(!(node instanceof MetaNode)) {
	    			continue;
	    		}
	    		if(node.getKey().equals(key)) {
	    			user.data().remove(node);
	    			break;
	    		}
	    	}
	    }
	    api.getUserManager().saveUser(user);
	}
	
}
