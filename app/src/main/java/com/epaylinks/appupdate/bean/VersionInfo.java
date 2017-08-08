package com.epaylinks.appupdate.bean;

import java.io.Serializable;

public class VersionInfo extends ApkUpdateBase implements Serializable {
	private String isoptional;
	private String localVersion;
	private String mustUpdate;

	public String getMustUpdate() {
		return mustUpdate;
	}

	public void setMustUpdate(String mustUpdate) {
		this.mustUpdate = mustUpdate;
	}

	public String getIsoptional() {
		return isoptional;
	}

	public void setIsoptional(String isoptional) {
		this.isoptional = isoptional;
	}

	public String getLocalVersion() {
		return localVersion;
	}

	public void setLocalVersion(String localVersion) {
		this.localVersion = localVersion;
	}

}
