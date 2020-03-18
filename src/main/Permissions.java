package main;

public class Permissions {

	public static final String PREFIX = "globalchat.";
	
	public static final String HELPER_P = "helper.";
	
	public static final String ADMIN_P = "admin.";
	
	public static final String ADMINCHAT = PREFIX+HELPER_P+"adminchat";
	
	public static final String MUTE = PREFIX+HELPER_P+"mute";
	
	public static final String MUTE_ALL = PREFIX+ADMIN_P+"muteall";
	
	public static final String KICK = PREFIX+HELPER_P+"kick";
	
	public static final String EVENT = PREFIX+HELPER_P+"event";
	
	public static final String SET_COLOR = PREFIX+HELPER_P+"setcolor";
	
	public static final String SET_COLOR_OTHERS = PREFIX+ADMIN_P+"setcolorothers";
	
	public static final String SOCIAL_SPY = PREFIX+ADMIN_P+"socialspy";
	
	public static final String RELOAD = PREFIX+ADMIN_P+"reload";
	
	public static final String CHANGE_NICK = PREFIX+ADMIN_P+"changenick";
	
	public static final String NO_MESSAGE_CHECK = PREFIX+ADMIN_P+"nocheck";
	
	public static final String COLORED_MESSAGES = PREFIX+ADMIN_P+"colored";
	
}
