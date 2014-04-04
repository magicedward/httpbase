package io.viva.base.info;

public class UpdateInfo {
	private boolean isToUpdate;
	private String updateUrl;
	private int versionCode;
	private String versionName;
	private String description;

	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String paramString) {
		this.versionName = paramString;
	}

	public boolean isToUpdate() {
		return this.isToUpdate;
	}

	public void setToUpdate(boolean paramBoolean) {
		this.isToUpdate = paramBoolean;
	}

	public String getUpdateUrl() {
		return this.updateUrl;
	}

	public void setUpdateUrl(String paramString) {
		this.updateUrl = paramString;
	}

	public int getVersionCode() {
		return this.versionCode;
	}

	public void setVersionCode(int paramInt) {
		this.versionCode = paramInt;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String paramString) {
		this.description = paramString;
	}
}