package main.playerdata;

import java.util.ArrayList;
import java.util.List;

import main.config.GlobalChatConfiguration;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GPlayer {

	private ProxiedPlayer player;
	private String prefix;
	private String suffix;
	private char colorIndex;
	private String lastMessage;
	private String lastPrivateMsgReceiverName;
	private boolean muted;
	private boolean activeAdminChat;
	private boolean socialSpy;
	private boolean joinedServer;
	private long lastMessageTimestamp;
	private String customNick;
	private List<String> ignoredPlayersNames;
	
	public GPlayer(ProxiedPlayer pp) {
		
		this.player = pp;
		this.prefix = "";
		this.suffix = "";
		this.colorIndex = GlobalChatConfiguration.config.getString("defaultMessageColor").charAt(0);
		this.lastMessage = "";
		this.lastMessageTimestamp = 0;
		this.muted = false;
		this.activeAdminChat = false;
		this.socialSpy = false;
		this.ignoredPlayersNames = new ArrayList<String>();
		this.joinedServer = false;
		
	}
	
	public ProxiedPlayer getProxiedPlayer() {
		return player;
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
		return activeAdminChat;
	}

	public void setActivedAdminChat(boolean activedAdminChat) {
		this.activeAdminChat = activedAdminChat;
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

	public String getCustomNick() {
		return customNick;
	}

	public void setCustomNick(String customNick) {
		this.customNick = customNick;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
}
