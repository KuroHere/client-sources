package me.protocol_client.friendsList;

public class Friend {
	private String	name;
	private String	alias;

	public Friend(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}

	public void setName(String s) {
		name = s;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
