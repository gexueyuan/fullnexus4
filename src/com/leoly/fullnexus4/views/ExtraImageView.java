/**
 * <pre>
 * Title: 		ExtraImageView.java
 * Project: 	FullNexus4
 * Type:		com.leoly.fullnexus4.ExtraImageView
 * Author:		255507
 * Create:	 	2013-1-16 ÏÂÎç2:57:54
 * Copyright: 	Copyright (c) 2013
 * Company:		
 * <pre>
 */
package com.leoly.fullnexus4.views;

import android.content.Context;
import android.widget.ImageView;

/**
 * <pre>
 * </pre>
 * @author 255507
 * @version 1.0, 2013-1-16
 */
public class ExtraImageView extends ImageView
{
	private String pkgName;

	public ExtraImageView(Context context)
	{
		super(context);
	}

	public void setPkgName(String pkgName)
	{
		this.pkgName = pkgName;
	}

	public String getPkgName()
	{
		return pkgName;
	}
}
