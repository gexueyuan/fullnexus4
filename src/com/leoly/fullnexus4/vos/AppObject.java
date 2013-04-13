package com.leoly.fullnexus4.vos;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class AppObject
{
	private String name; // 应用名

	private String packageName; // 包名

	private ApplicationInfo info;

	public ApplicationInfo getInfo()
	{
		return info;
	}

	public void setInfo(ApplicationInfo info)
	{
		this.info = info;
	}

	private Drawable icon;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	public Drawable getIcon()
	{
		return icon;
	}

	public void setIcon(Drawable icon)
	{
		this.icon = icon;
	}

}
