package net.theelder.plugins.Spleef.enums;

public enum Phase {

	LOBBY(true), PRE_GAME(true), IN_GAME(false), ENDING(false);
	
	public boolean canJoin;
	
	Phase(boolean canJoin) {
		this.canJoin = canJoin;
	}
	
}
