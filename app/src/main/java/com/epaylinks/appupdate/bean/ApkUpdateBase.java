package com.epaylinks.appupdate.bean;

import java.io.Serializable;

public class ApkUpdateBase implements Serializable {
	private String desc;
	private String apkPath;
	private String version;
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getApkPath() {
		return apkPath;
	}

	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
