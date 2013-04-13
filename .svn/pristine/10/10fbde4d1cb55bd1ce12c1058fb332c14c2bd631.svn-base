package com.leoly.fullnexus4.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import android.util.Log;

public class BuildFileOperate {
	private static final String BUILD_PROPER = "/system/build.prop";

	private static LinkedList<String> lines = new LinkedList<String>();

	private static BuildFileOperate instance;

	public static BuildFileOperate getInstance() {
		if (null == instance) {
			instance = new BuildFileOperate();
			readLines();
		}

		return instance;
	}

	private static void readLines() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(BUILD_PROPER));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (null == line || line.trim().length() < 1) {
					continue;
				}

				lines.add(line);
			}
		} catch (Exception e) {
			Log.e(TopConstants.APP_TAG, "Read /system/build.prop occur error!",
					e);
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void destroy() {
		lines.clear();
		instance = null;
	}

	public void appendLine(String line) {
		if (lines.isEmpty()) {
			return;
		}

		boolean isExist = false;
		for (String tempLine : lines) {
			if (tempLine.startsWith("qemu.hw.mainkeys")) {
				tempLine = "qemu.hw.mainkeys=1";
				isExist = true;
				break;
			}
		}

		if (!isExist) {
			lines.add(line);
		}
		File file = new File(BUILD_PROPER);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			for (String temp : lines) {
				writer.println(temp);
			}
		} catch (Exception e) {
			Log.e(TopConstants.APP_TAG,
					"Append custon properties to /system/build.prop occur error!",
					e);
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (Exception e) {
					Log.e(TopConstants.APP_TAG, "Close writer occur error!", e);
				}
			}
		}
	}

	public void removeLine(String line) {

		File file = new File(BUILD_PROPER);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			for (String temp : lines) {
				if (temp.equals(line.trim())) {
					continue;
				}
				writer.println(temp);
			}
		} catch (Exception e) {
			Log.e(TopConstants.APP_TAG,
					"Remove custon properties from /system/build.prop occur error!",
					e);
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (Exception e) {
					Log.e(TopConstants.APP_TAG, "Close writer occur error!", e);
				}
			}
		}

	}
}