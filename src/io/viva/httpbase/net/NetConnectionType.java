package io.viva.httpbase.net;

public enum NetConnectionType {
	wifi("wifi"), ether("ether"), mobile("mobile"), none("none");

	private String code;
	private String name;

	private NetConnectionType(String paramString) {
		this.code = paramString;
	}

	public final String getCode() {
		return this.code;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(String paramString) {
		this.name = paramString;
	}

	public static NetConnectionType getTypeByCode(String paramString) {
		NetConnectionType[] arrayOfNetConnectionType1 = values();
		for (NetConnectionType localNetConnectionType : arrayOfNetConnectionType1)
			if (localNetConnectionType.getCode().equalsIgnoreCase(paramString))
				return localNetConnectionType;
		return none;
	}
}