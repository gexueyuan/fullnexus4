package com.leoly.fullnexus4.utils;

import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class RootContext
{
	private static RootContext instance = null;

	InputStream i;

	String mShell;

	OutputStream o;

	Process p;

	private RootContext(String paramString) throws Exception
	{
		this.mShell = paramString;
		init();
	}

	public static RootContext getInstance()
	{
		if (instance == null)
		{
			try
			{
				instance = new RootContext("su");
				return instance;
			}
			catch (Exception localException1)
			{
				try
				{
					instance = new RootContext("/system/xbin/su");
				}
				catch (Exception localException2)
				{
					try
					{
						instance = new RootContext("/system/bin/su");
					}
					catch (Exception localException3)
					{
						Log.e(TopConstants.APP_TAG, "Get root permission error!", localException3);
					}
				}
			}
		}

		return instance;
	}

	private void init() throws Exception
	{
		if ((this.p != null) && (this.o != null))
		{
			this.o.flush();
			this.o.close();
			this.i.close();
			this.p.destroy();
		}
		this.p = Runtime.getRuntime().exec(this.mShell);
		this.o = this.p.getOutputStream();
		this.i = this.p.getInputStream();
	}

	public void runCommand(String paramString)
	{
		try
		{
			this.o.write(("LD_LIBRARY_PATH=/vendor/lib:/system/lib " + paramString + "\n").getBytes("ASCII"));
		}
		catch (Exception localException1)
		{
			try
			{
				init();
			}
			catch (Exception localException2)
			{
				Log.e(TopConstants.APP_TAG, "Execute command [" + paramString + "] error!", localException2);
			}
		}
	}

	public boolean runCommandWithResult(String paramString)
	{
		boolean j = false;
		try
		{
			int k = this.i.available();
			this.o.write(("LD_LIBRARY_PATH=/vendor/lib:/system/lib " + paramString + "\n").getBytes("ASCII"));
			int l = this.i.available();
			if (l > k)
				j = true;
			return j;
		}
		catch (Exception localException1)
		{
			try
			{
				init();
				j = false;
			}
			catch (Exception localException2)
			{
				j = false;
			}
		}

		return j;
	}
}
