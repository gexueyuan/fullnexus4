package com.leoly.fullnexus4.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class RunCommand
{
	public static String CmdMountDataRW;

	public static String CmdMountFactoryRW;

	public static String CmdMountSystemRW = " mount -o remount,rw /dev/block/platform/omap/omap_hsmmc.0/by-name/system /system ; ";

	public static final String SCRIPT_NAME = "surunner.sh";

	static
	{
		CmdMountDataRW = " mount -o remount,rw /dev/block/platform/omap/omap_hsmmc.0/by-name/userdata /data ; ";
		CmdMountFactoryRW = " mount -o remount,rw /dev/block/platform/omap/omap_hsmmc.0/by-name/efs /factory ; ";
	}

	public static String getBusyBoxPath(Context paramContext)
	{
		return new File(paramContext.getFilesDir().getAbsolutePath(), "busybox").getAbsolutePath();
	}

	public static File getDataStorageDirectory(Context paramContext)
	{
		return paramContext.getFilesDir();
	}

	public static boolean hasUnpackBusyBox(Context paramContext)
	{
		return new File(getBusyBoxPath(paramContext)).exists();
	}

	public static boolean haveRoot()
	{
		return runSuCommandReturnBoolean("echo 1");
	}

	public static boolean mountSystemRW()
	{
		boolean i = false;
		try
		{
			int j = runSuCommandNoScriptWrapper(CmdMountSystemRW);
			if (j != 0)
				return i;
			i = true;
			return i;
		}
		catch (IOException localIOException)
		{
			i = false;
		}
		catch (InterruptedException localInterruptedException)
		{
			i = false;
		}
		return i;
	}

	public static int runSuCommand(Context paramContext, String paramString) throws IOException, InterruptedException
	{
		return runSuCommandAsync(paramContext, paramString).waitFor();
	}

	public static Process runSuCommandAsync(Context paramContext, String paramString) throws IOException
	{
		DataOutputStream localDataOutputStream = new DataOutputStream(paramContext.openFileOutput("surunner.sh", 0));
		localDataOutputStream.writeBytes(paramString);
		localDataOutputStream.close();
		String[] arrayOfString = new String[3];
		arrayOfString[0] = "su";
		arrayOfString[1] = "-c";
		arrayOfString[2] = (". " + paramContext.getFilesDir().getAbsolutePath() + "/" + "surunner.sh");
		return Runtime.getRuntime().exec(arrayOfString);
	}

	public static int runSuCommandNoScriptWrapper(String paramString) throws IOException, InterruptedException
	{
		String[] arrayOfString = { "su", "-c", paramString };
		return Runtime.getRuntime().exec(arrayOfString).waitFor();
	}

	public static boolean runSuCommandReturnBoolean(String paramString)
	{
		boolean i = false;
		try
		{
			Process localProcess = Runtime.getRuntime().exec("su");
			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes(paramString + "\n");
			localDataOutputStream.flush();
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			int j = localProcess.waitFor();
			if (j != 0)
				return i;
			i = true;
			return i;
		}
		catch (InterruptedException localInterruptedException)
		{
			i = false;
		}
		catch (IOException localIOException)
		{
			i = false;
		}

		return i;
	}

	public static String unpackFile(Context paramContext, String paramString1, String paramString2)
	{
		if ((paramString2 == null) || (paramString2.trim().equals("")))
			paramString2 = "777";
		File localFile = new File(paramContext.getFilesDir().getAbsolutePath(), paramString1);
		byte[] arrayOfByte = new byte[10240];
		try
		{
			InputStream localInputStream = paramContext.getResources().getAssets().open("files/" + paramString1);
			FileOutputStream localFileOutputStream = new FileOutputStream(localFile, false);
			int i = localInputStream.read(arrayOfByte);
			if (i < 0)
			{
				localFileOutputStream.close();
				localInputStream.close();
				runSuCommandReturnBoolean("chmod " + paramString2 + " " + localFile.getAbsolutePath());
				return localFile.getAbsolutePath();
			}
			localFileOutputStream.write(arrayOfByte, 0, i);
		}
		catch (IOException localIOException)
		{
			localIOException.printStackTrace();
		}
		return null;
	}

	public static String unpackFile(Context paramContext, String paramString1, String paramString2, String paramString3)
	{
		if ((paramString3 == null) || (paramString3.trim().equals("")))
			paramString3 = "777";
		File localFile = new File(paramContext.getFilesDir().getAbsolutePath(), paramString1);
		byte[] arrayOfByte = new byte[10240];
		try
		{
			InputStream localInputStream = paramContext.getResources().getAssets().open("files/" + paramString1);
			FileOutputStream localFileOutputStream = new FileOutputStream(localFile, false);
			int i = localInputStream.read(arrayOfByte);
			if (i < 0)
			{
				localFileOutputStream.close();
				localInputStream.close();
				String str = null;
				if (paramString2 != null)
					str = null + "chown " + paramString2 + " " + localFile.getAbsolutePath() + " ; ";
				if (paramString3 != null)
					str = str + "chmod " + paramString3 + " " + localFile.getAbsolutePath() + " ; ";
				if (str != null)
					runSuCommandReturnBoolean(str);
				return localFile.getAbsolutePath();
			}
			localFileOutputStream.write(arrayOfByte, 0, i);
		}
		catch (IOException localIOException)
		{
			localIOException.printStackTrace();
		}
		return null;
	}

	public static String warpCmdWithBusybox(Context paramContext, String paramString)
	{
		return getBusyBoxPath(paramContext) + " " + paramString;
	}
}

/*
 * Location: D:\dex2jar\GNFULLV2-dex2jar.jar Qualified Name:
 * cn.kyle.gn.NavBar.C JD-Core Version: 0.5.4
 */