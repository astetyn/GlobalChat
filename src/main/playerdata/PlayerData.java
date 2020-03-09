package main.playerdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import main.ChatPrefabrics;
import main.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerData {

	private ProxiedPlayer proxiedPlayer;
	private String prefix;
	private char colorIndex;
	private String lastMessage;
	private String lastPrivateMsgReceiverName;
	private boolean muted;
	private boolean activedAdminChat;
	private boolean socialSpy;
	private boolean joinedServer;
	private TimerTask tempMuteTask;
	private long lastMessageTimestamp;
	private String customNick;

	private List<String> ignoredPlayersNames;
	private long muteTimestamp;
	private double muteDuration;
	
	public PlayerData(ProxiedPlayer pp) {
		
		this.proxiedPlayer = pp;
		this.prefix = "";
		this.colorIndex = '7';
		this.lastMessage = "";
		this.lastMessageTimestamp = 0;
		this.muted = false;
		this.activedAdminChat = false;
		this.socialSpy = false;
		this.ignoredPlayersNames = new ArrayList<String>();
		this.joinedServer = false;
		this.muteTimestamp = 0;
		this.muteDuration = 0;
		
	}

	public void syncPrefix() {
		MetaLoader ml = new MetaLoader(proxiedPlayer);
    	prefix = ml.loadPrefix();
	}
	
	public void syncMetaValues() {
    	
		MetaLoader ml = new MetaLoader(proxiedPlayer);

		if(!proxiedPlayer.hasPermission("globalchat.setcolor")) {
			ml.removeOwnMeta("globalchat-color");
		}
		
    	String colorIndexStr = ml.loadMeta("globalchat-color");
    	if(colorIndexStr==null) {
    		colorIndexStr = "7";
    	}
    	
    	colorIndex = colorIndexStr.charAt(0);
    	
    	String nickname = ml.loadMeta("globalchat-nick");
    	if(nickname!=null) {
    		customNick = nickname;
    	}
    	
    	String muteTimeRemainingStr = ml.loadMeta("globalchat-mtr");
    	if(muteTimeRemainingStr==null) {
    		muteTimeRemainingStr = "0";
    	}
		int muteTimeRemaining = Integer.parseInt(muteTimeRemainingStr);
		if(muteTimeRemaining==-1) {
			muted = true;
			ml.saveMeta("globalchat-mtr", "0");
		}else if(muteTimeRemaining>0) {
			String leaveTimestampStr = ml.loadMeta("globalchat-lts");
			long leaveTimestamp = Long.parseLong(leaveTimestampStr);
			long currentTime = System.currentTimeMillis();
			long difference = currentTime - leaveTimestamp;
			int differenceSec = (int) (difference/1000);
			muteTimeRemaining -= differenceSec;
			if(muteTimeRemaining>0) {
				muted = true;
				muteTimestamp = System.currentTimeMillis();
				muteDuration = muteTimeRemaining;
				Timer timer = new Timer();
				TimerTask ts = new TimerTask() {
					@Override
					public void run() {
						muted = false;
						proxiedPlayer.sendMessage(TextComponent.fromLegacyText(ChatPrefabrics.SILENCE +
								ChatColor.GRAY + "You are unsilenced now. Silence time passed."));
						Main.LOG.info("User "+proxiedPlayer.getName()+" unmuted due to time reach.");
					}
				};
				timer.schedule(ts, (int)(muteTimeRemaining*1000));
				tempMuteTask = ts;
			}
			ml.saveMeta("globalchat-mtr", "0");
		}
		
		String socialSpyStr = ml.loadMeta("globalchat-socialspy");
		socialSpy = Boolean.parseBoolean(socialSpyStr);
	}
	
	public void saveMetaValues() {
		
		MetaLoader ml = new MetaLoader(proxiedPlayer);
		
		if(muted) {
			long currentTime = System.currentTimeMillis();
			long remaining = (long) (muteTimestamp + muteDuration*1000 - currentTime);
			if(remaining>0) {
				ml.saveMeta("globalchat-lts", currentTime+"");
				ml.saveMeta("globalchat-mtr", remaining/1000+"");
			}else {
				ml.saveMeta("globalchat-mtr", "-1");
			}
		}
		ml.saveMeta("globalchat-socialspy", socialSpy+"");
	}
	
	public ProxiedPlayer getProxiedPlayer() {
		return proxiedPlayer;
	}

	public String getPrefix() {
		return prefix;
	}

	public char getColorIndex() {
		return colorIndex;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setColorIndex(char colorIndex) {
		this.colorIndex = colorIndex;
	}

	public long getLastTimestamp() {
		return lastMessageTimestamp;
	}

	public void setLastTimestamp(long lastTimestamp) {
		this.lastMessageTimestamp = lastTimestamp;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public boolean hasActivedAdminChat() {
		return activedAdminChat;
	}

	public void setActivedAdminChat(boolean activedAdminChat) {
		this.activedAdminChat = activedAdminChat;
	}

	public String getLastPrivateMsgReceiverName() {
		return lastPrivateMsgReceiverName;
	}

	public void setLastPrivateMsgReceiverName(String lastPrivateMsgReceiverName) {
		this.lastPrivateMsgReceiverName = lastPrivateMsgReceiverName;
	}

	public boolean hasSocialSpy() {
		return socialSpy;
	}

	public void setSocialSpy(boolean socialSpy) {
		this.socialSpy = socialSpy;
	}

	public List<String> getIgnoredPlayersNames() {
		return ignoredPlayersNames;
	}

	public boolean isJoinedServer() {
		return joinedServer;
	}

	public void setJoinedServer(boolean joinedServer) {
		this.joinedServer = joinedServer;
	}

	public TimerTask getTempMuteTask() {
		return tempMuteTask;
	}

	public void setTempMuteTask(TimerTask tempMuteTask) {
		this.tempMuteTask = tempMuteTask;
	}

	public long getMuteTimestamp() {
		return muteTimestamp;
	}

	public void setMuteTimestamp(long muteTimestamp) {
		this.muteTimestamp = muteTimestamp;
	}

	public double getMuteDuration() {
		return muteDuration;
	}

	public void setMuteDuration(double muteDuration) {
		this.muteDuration = muteDuration;
	}

	public String getCustomNick() {
		return customNick;
	}

	public void setCustomNick(String customNick) {
		this.customNick = customNick;
	}
	
}
