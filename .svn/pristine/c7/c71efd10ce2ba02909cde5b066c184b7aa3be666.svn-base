/**
 * <pre>
 * Title: 		VibratorAdaptor.java
 * Project: 	FullNexus4
 * Type:		com.leoly.fullnexus4.adaptors.VibratorAdaptor
 * Author:		255507
 * Create:	 	2013-2-1 下午6:28:41
 * Copyright: 	Copyright (c) 2013
 * Company:		
 * <pre>
 */
package com.leoly.fullnexus4.adaptors;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * <pre>
 * </pre>
 * @author 255507
 * @version 1.0, 2013-2-1
 */
public class VibratorAdaptor
{
	private Vibrator vibrator;

	private boolean needVibrate = false;

	public VibratorAdaptor(Context context, boolean needVibrate)
	{
		if (null == context)
		{
			return;
		}

		vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		this.needVibrate = needVibrate;
	}

	public void vibrator(long time)
	{
		if (needVibrate)
		{
			vibrator.vibrate(time);
		}
	}

	public void vibrator(long[] time, int repeat)
	{
		if (needVibrate)
		{
			vibrator.vibrate(time, repeat);
		}
	}
}
