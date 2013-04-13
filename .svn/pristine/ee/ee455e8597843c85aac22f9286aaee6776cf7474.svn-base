package com.leoly.fullnexus4.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.leoly.fullnexus4.R;
import com.leoly.fullnexus4.services.ICallBack;
import com.leoly.fullnexus4.views.ExtraImageView;
import com.leoly.fullnexus4.vos.AppObject;

/**
 * @author culin003
 */
public class ButtonFactory {
	public final static byte BACK = 100;

	public final static byte HOME = 101;

	public final static byte RECENT = 102;

	public final static byte MENU = 103;

	public final static byte VOLUMN_UP = 105;

	public final static byte VOLUMN_DOWN = 106;

	public final static byte POWER = 104;

	public final static byte SEARCH = 107;

	public final static byte HIDE = 108;

	public final static byte[] SYS_BTNS = new byte[] { BACK, HOME, RECENT,
			MENU, POWER, SEARCH, HIDE };

	private static ButtonFactory factory = null;

	private static Context context;

	private final static Animation animation = new ScaleAnimation(1f, 0.7f, 1f,
			0.7f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
			0.5f);

	private static EventHandler handler = null;

	private static ActivityManager am;

	private ICallBack callback;

	public static ButtonFactory getInstance(Context context, int location,
			ICallBack callback) {
		if (null == factory) {
			factory = new ButtonFactory();
			animation.setDuration(100);
			animation.setRepeatCount(1);
			animation.setRepeatMode(AlphaAnimation.REVERSE);

		}
		factory.callback = callback;
		factory.handler = new EventHandler(context);
		factory.context = context;
		am = ((ActivityManager) context.getSystemService("activity"));
		return factory;
	}

	public static List<AppObject> getSysButtons(Context ctx) {
		List<AppObject> btns = new ArrayList<AppObject>();
		AppObject obj = null;
		for (byte btnId : SYS_BTNS) {
			obj = new AppObject();
			switch (btnId) {
			case BACK:
				obj.setName(ctx.getString(R.string.backKey));
				obj.setIcon(ctx.getResources().getDrawable(R.drawable.ic_back));
				break;
			case HOME:
				obj.setName(ctx.getString(R.string.homeKey));
				obj.setIcon(ctx.getResources().getDrawable(R.drawable.ic_home));
				break;
			case RECENT:
				obj.setName(ctx.getString(R.string.appsKey));
				obj.setIcon(ctx.getResources()
						.getDrawable(R.drawable.ic_recent));
				break;
			case MENU:
				obj.setName(ctx.getString(R.string.menuKey));
				obj.setIcon(ctx.getResources().getDrawable(R.drawable.ic_menu));
				break;
			case POWER:
				obj.setName(ctx.getString(R.string.powerKey));
				obj.setIcon(ctx.getResources().getDrawable(
						R.drawable.ic_screen_off));
				break;
			case SEARCH:
				obj.setName(ctx.getString(R.string.searchKey));
				obj.setIcon(ctx.getResources()
						.getDrawable(R.drawable.ic_search));
				break;
			case HIDE:
				obj.setName(ctx.getString(R.string.hideKey));
				obj.setIcon(ctx.getResources().getDrawable(
						R.drawable.ic_page_end));
				break;
			default:
				break;
			}

			obj.setPackageName(TopConstants.VKEY_PREFIX + btnId);
			btns.add(obj);
		}

		return btns;
	}

	public static List<ExtraImageView> getCustomKeys(String customBtns,
			Context ctx) {
		List<ExtraImageView> ivKeys = new ArrayList<ExtraImageView>();
		if (null == customBtns || customBtns.trim().isEmpty()) {
			return ivKeys;
		}

		ExtraImageView iv = null;
		try {
			JSONArray array = new JSONArray(customBtns);
			int length = array.length();
			String keyCode = null;
			for (int i = 0; i < length; i++) {
				keyCode = array.getString(i);
				iv = new ExtraImageView(ctx);
				if (keyCode.startsWith(TopConstants.VKEY_PREFIX)) {
					byte btnId = Byte.valueOf(keyCode
							.substring(TopConstants.VKEY_PREFIX.length()));
					switch (btnId) {
					case BACK:
						iv.setImageResource(R.drawable.ic_back);
						break;
					case HOME:
						iv.setImageResource(R.drawable.ic_home);
						break;
					case MENU:
						iv.setImageResource(R.drawable.ic_menu);
						break;
					case RECENT:
						iv.setImageResource(R.drawable.ic_recent);
						break;
					case POWER:
						iv.setImageResource(R.drawable.ic_screen_off);
						break;
					case SEARCH:
						iv.setImageResource(R.drawable.ic_search);
						break;
					case HIDE:
						iv.setImageResource(R.drawable.ic_page_end);
						break;
					default:
						break;
					}
				} else {
					String pkgName = keyCode.substring(TopConstants.PKG_PREFIX
							.length());
					try {
						iv.setImageDrawable(ctx.getPackageManager()
								.getApplicationIcon(pkgName));
					} catch (NameNotFoundException e) {
						Log.e(TopConstants.APP_TAG,
								"Can not load package icon!", e);
					}
				}

				iv.setPkgName(keyCode);
				ivKeys.add(iv);
			}

		} catch (JSONException e) {
			Log.e(TopConstants.APP_TAG, "Bad custom key config!", e);
		}

		return ivKeys;
	}

	public List<ExtraImageView> getButtons(String customBtns) {
		List<ExtraImageView> btns = new ArrayList<ExtraImageView>();
		// 配置为空时，自动加入系统按钮
		if (null == customBtns || "[]".equals(customBtns.trim())) {
			for (byte btnId : SYS_BTNS) {
				btns.add(createButton(btnId));
			}

			return btns;
		}

		try {
			JSONArray array = new JSONArray(customBtns);
			int length = array.length();
			String keyCode = null;
			for (int i = 0; i < length; i++) {
				keyCode = array.getString(i);
				if (keyCode.startsWith(TopConstants.VKEY_PREFIX)) {
					byte btnId = Byte.valueOf(keyCode
							.substring(TopConstants.VKEY_PREFIX.length()));
					btns.add(createButton(btnId));
				} else {
					String pkgName = keyCode.substring(TopConstants.PKG_PREFIX
							.length());
					btns.add(createButton(pkgName));
				}
			}
		} catch (JSONException e) {
			Log.e(TopConstants.APP_TAG, "Bad custom key config!", e);
			return btns;
		}

		return btns;
	}

	private ExtraImageView createButton(final String pkgName) {
		final ExtraImageView iv = new ExtraImageView(context);
		final PackageManager manager = context.getPackageManager();
		try {
			iv.setImageDrawable(manager.getApplicationIcon(pkgName));
		} catch (NameNotFoundException e1) {
			Log.e(TopConstants.APP_TAG, "Can not load package icon!", e1);
		}
		iv.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				iv.startAnimation(animation);
				Intent intent = manager.getLaunchIntentForPackage(pkgName);
				try {
					context.startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(context,
							context.getString(R.string.canNotRunApp),
							Toast.LENGTH_SHORT);
				}
			}
		});
		iv.setPkgName(TopConstants.PKG_PREFIX + pkgName);
		return iv;
	}

	private ExtraImageView createButton(byte tag) {
		final ExtraImageView iv = new ExtraImageView(context);
		iv.setPkgName(TopConstants.VKEY_PREFIX + tag);
		boolean isCreate = true;
		switch (tag) {
		case BACK:
			iv.setImageResource(R.drawable.ic_back);
			iv.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					iv.startAnimation(animation);
					doKeyAction(KeyEvent.KEYCODE_BACK);
				}
			});

			iv.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View view) {
					iv.startAnimation(animation);
					doKillAppAction();
					return true;
				}
			});
			break;
		case HOME:
			iv.setImageResource(R.drawable.ic_home);
			iv.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					iv.startAnimation(animation);
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addCategory(Intent.CATEGORY_HOME);
					context.startActivity(intent);
					if (null != callback) {
						callback.run();
					}
				}
			});
			break;
		case RECENT:
			iv.setImageResource(R.drawable.ic_recent);
			iv.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					iv.startAnimation(animation);
					doShowRecentApps();
				}
			});
			iv.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View view) {
					iv.startAnimation(animation);
					doKeyAction(KeyEvent.KEYCODE_MENU);
					if (null != callback) {
						callback.hideAllKeys();
					}
					return true;
				}
			});
			break;
		case MENU:
			iv.setImageResource(R.drawable.ic_menu);
			iv.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					iv.startAnimation(animation);
					doKeyAction(KeyEvent.KEYCODE_MENU);
				}
			});
			break;
		case POWER:
			iv.setImageResource(R.drawable.ic_screen_off);
			iv.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					iv.startAnimation(animation);
					doKeyAction(KeyEvent.KEYCODE_POWER);
				}
			});
			iv.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View view) {
					iv.startAnimation(animation);
					doLongPressKey(KeyEvent.KEYCODE_POWER);
					return true;
				}
			});
			break;
		case SEARCH:
			iv.setImageResource(R.drawable.ic_search);
			iv.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					iv.startAnimation(animation);
					doKeyAction(KeyEvent.KEYCODE_SEARCH);
				}
			});
			break;
		case HIDE:
			iv.setImageResource(R.drawable.ic_page_end);
			iv.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					if (null != callback) {
						callback.hideAllKeys();
					}
				}
			});
			break;
		default:
			isCreate = false;
			break;
		}

		return isCreate ? iv : null;
	}

	private void doKillAppAction() {
		if (null != callback) {
			callback.run();
		}
		final List<RunningTaskInfo> rti = am.getRunningTasks(1);
		final String cmpName = ((ActivityManager.RunningTaskInfo) rti.get(0)).baseActivity
				.getPackageName();
		String temp = new String(cmpName).toLowerCase();
		// 不杀启动器
		if (temp.contains("launcher") || temp.contains("fullnexus4")) {
			return;
		}
		doKeyAction(KeyEvent.KEYCODE_HOME);
		new Timer().schedule(new TimerTask() {
			public void run() {
				RootContext.getInstance()
						.runCommand("am force-stop " + cmpName);
			}
		}, 1000L);
	}

	private void doKeyAction(int paramInt) {
		handler.sendKeys(paramInt);
		if (null != callback) {
			callback.run();
		}
	}

	private void doLongPressKey(int paramInt) {
		handler.sendDownTouchKeys(paramInt);
		if (null != callback) {
			callback.run();
		}
	}

	private void doShowRecentApps() {
		try {
			Class localClass1 = Class.forName("android.os.ServiceManager");
			IBinder localIBinder = (IBinder) localClass1.getMethod(
					"getService", new Class[] { String.class }).invoke(
					localClass1, new Object[] { "statusbar" });
			Class localClass2 = Class.forName(
					"com.android.internal.statusbar.IStatusBarService")
					.getClasses()[0];
			Object localObject = localClass2.getMethod("asInterface",
					new Class[] { IBinder.class }).invoke(null,
					new Object[] { localIBinder });
			localClass2.getMethod("toggleRecentApps", new Class[0]).invoke(
					localObject, new Object[0]);
		} catch (Exception e) {
			doKeyAction(KeyEvent.KEYCODE_APP_SWITCH);
		} finally {
			if (null != callback) {
				callback.run();
			}
		}
	}

	public static String getKeysString(LinkedList<String> keys) {
		JSONArray array = new JSONArray(keys);
		return array.toString();
	}

	public static int getRealValue(int value, int type) {
		int realValue = 0;
		switch (type) {
		case 1:
			realValue = value * 5 + 80;
			break;
		case 2:
			realValue = value * 1 + 5;
			break;
		default:
			break;
		}

		return realValue;
	}

	public static int getRealColor(int value) {
		int c = Color.BLACK;
		switch (value) {
		case 0:
			c = Color.BLACK;
			break;
		case 1:
			c = Color.RED;
			break;
		case 2:
			c = Color.GREEN;
			break;
		case 3:
			c = Color.BLUE;
			break;
		case 4:
			c = Color.WHITE;
			break;
		case 5:
			c = Color.YELLOW;
			break;
		case 6:
			c = Color.GRAY;
			break;
		case 7:
			c = Color.CYAN;
			break;
		default:
			break;
		}

		return c;
	}
}
