/**
 * <pre>
 * Title: 		TopActivity.java
 * Project: 	TopService
 * Type:		com.leoly.TopActivity
 * Author:		255507
 * Create:	 	2013-1-8 下午6:43:10
 * Copyright: 	Copyright (c) 2013
 * Company:		
 * <pre>
 */
package com.leoly.fullnexus4.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leoly.fullnexus4.R;
import com.leoly.fullnexus4.adaptors.SeekbarSliderEndAdaptor;
import com.leoly.fullnexus4.services.FullNexus4Service;
import com.leoly.fullnexus4.utils.BuildFileOperate;
import com.leoly.fullnexus4.utils.ButtonFactory;
import com.leoly.fullnexus4.utils.RunCommand;
import com.leoly.fullnexus4.utils.TopConstants;
import com.leoly.fullnexus4.views.ExtraImageView;

/**
 * <pre>
 * </pre>
 * 
 * @author 255507
 * @version 1.0, 2013-1-8
 */
public class FullNexus4Activity extends Activity {
	private final String HIDE_NAV_CMD = "qemu.hw.mainkeys=1";

	private Context context;

	private SharedPreferences prefer;

	private LinearLayout keyTempPanel;

	private LayoutParams lp = new LayoutParams(85, 85);

	private LinkedList<String> keys = new LinkedList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.main_setting);

		// 按键服务开关
		CheckBox toggle = (CheckBox) findViewById(R.id.toggleButton);
		prefer = getSharedPreferences(TopConstants.CONFIG_NAME, MODE_PRIVATE);
		toggle.setChecked(prefer.getBoolean(TopConstants.OPEN_KEY_SERVICE,
				false));
		toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton btn, boolean flag) {
				setSettings(TopConstants.OPEN_KEY_SERVICE, flag, true,
						flag ? R.string.keyServiceStarted
								: R.string.keyServiceStopted,
						TopConstants.BOOLEAN);
			}
		});

		// 全屏服务开关
		CheckBox fullScreen = (CheckBox) findViewById(R.id.toggleFullscreen);
		fullScreen.setChecked(prefer.getBoolean(TopConstants.OPEN_FULL_SCREEN,
				false));
		fullScreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton btn, boolean flag) {
				openFullScreen(flag);
			}
		});

		// 震动反馈开关
		CheckBox vibrate = (CheckBox) findViewById(R.id.isNeedVibrator);
		vibrate.setChecked(prefer.getBoolean(TopConstants.IS_NEED_VIBRATE,
				false));
		vibrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton btn, boolean flag) {
				setSettings(TopConstants.IS_NEED_VIBRATE, flag, true,
						flag ? R.string.isNeedVibrator : R.string.stopVibrator,
						TopConstants.BOOLEAN);
			}
		});

		// 自动隐藏按键设置
		SeekBar sb = (SeekBar) findViewById(R.id.hideTImeSeekBar);
		sb.setMax(10);
		int setedHideTime = prefer.getInt(TopConstants.HIDE_TIME, 5);
		sb.setProgress(setedHideTime);
		final TextView hideTime = (TextView) findViewById(R.id.hideTime);
		hideTime.setText(String.valueOf(setedHideTime));
		sb.setOnSeekBarChangeListener(new SeekbarSliderEndAdaptor() {
			public void onProgressChanged(SeekBar bar, int value, boolean isUser) {
				hideTime.setText(String.valueOf(bar.getProgress()));
				setSettings(TopConstants.HIDE_TIME, bar.getProgress(),
						TopConstants.INT);
			}
		});

		// 设置自定义按键
		Button restart = (Button) findViewById(R.id.restartKeyService);
		restart.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				restartKeyService();
			}
		});

		// 装载按键的容器
		keyTempPanel = (LinearLayout) findViewById(R.id.keyTempPanel);
		// 根据配置来获取自定义的按钮
		List<ExtraImageView> cks = ButtonFactory.getCustomKeys(
				prefer.getString(TopConstants.CUSTOM_KEYS, null),
				FullNexus4Activity.this);
		keys.clear();
		if (null != cks && !cks.isEmpty()) {
			for (final ExtraImageView civ : cks) {
				civ.setOnLongClickListener(new OnLongClickListener() {
					public boolean onLongClick(View view) {
						keyTempPanel.removeView(civ);
						keys.remove(civ.getPkgName());
						return true;
					}
				});

				keyTempPanel.addView(civ, keyTempPanel.getChildCount() - 1, lp);
				keys.add(civ.getPkgName());
			}
		}

		// 按键容器中的增加按钮
		ImageView resetKeys = (ImageView) findViewById(R.id.addBtn);
		resetKeys.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				addBtn();
			}
		});

		// 按键位置
		final TextView keyLocation = (TextView) findViewById(R.id.keyLocationText);
		SeekBar lsb = (SeekBar) findViewById(R.id.keyLocationSeekBar);
		lsb.setMax(2);
		int tempLocation = prefer.getInt(TopConstants.KEY_LOCATION,
				TopConstants.BUTTOM);
		lsb.setProgress(tempLocation);
		keyLocation.setText(getLocation(tempLocation));
		lsb.setOnSeekBarChangeListener(new SeekbarSliderEndAdaptor() {
			public void onProgressChanged(SeekBar bar, int value, boolean isUser) {
				keyLocation.setText(getLocation(bar.getProgress()));
				setSettings(TopConstants.KEY_LOCATION, bar.getProgress(),
						TopConstants.INT);
			}
		});

		// 保存设置
		Button saveBtn = (Button) findViewById(R.id.saveKeys);
		saveBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setSettings(TopConstants.CUSTOM_KEYS,
						ButtonFactory.getKeysString(keys), TopConstants.STRING);
			}
		});

		// 查看日志
		Button showLogBtn = (Button) findViewById(R.id.showChangeLog);
		showLogBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(FullNexus4Activity.this,
						ChangLogActivity.class);
				startActivity(intent);
			}
		});

		// 按键区高（宽）度
		final TextView keysetsHeight = (TextView) findViewById(R.id.keysetsHeight);
		SeekBar keysetsHeightBar = (SeekBar) findViewById(R.id.keysetsHeightBar);
		keysetsHeightBar.setMax(50);
		int tempKeyHeight = prefer.getInt(TopConstants.KEY_SET_HEIGHT, 10);
		keysetsHeightBar.setProgress(tempKeyHeight);
		keysetsHeight.setText(getRealValue(tempKeyHeight, 1));
		keysetsHeightBar
				.setOnSeekBarChangeListener(new SeekbarSliderEndAdaptor() {
					@Override
					public void onProgressChanged(SeekBar bar, int arg1,
							boolean arg2) {
						String tempHeight = getRealValue(bar.getProgress(), 1);
						keysetsHeight.setText(tempHeight);
						setSettings(TopConstants.KEY_SET_HEIGHT,
								bar.getProgress(), TopConstants.INT);
					}
				});

		// 按钮间隔
		final TextView btnSpace = (TextView) findViewById(R.id.btnSpace);
		SeekBar btnSpaceBar = (SeekBar) findViewById(R.id.btnSpaceBar);
		btnSpaceBar.setMax(100);
		int tempBtnSpace = prefer.getInt(TopConstants.BUTTON_SPACE, 5);
		btnSpaceBar.setProgress(tempBtnSpace);
		btnSpace.setText(getRealValue(tempBtnSpace, 2));
		btnSpaceBar.setOnSeekBarChangeListener(new SeekbarSliderEndAdaptor() {
			@Override
			public void onProgressChanged(SeekBar bar, int arg1, boolean arg2) {
				String tempHeight = getRealValue(bar.getProgress(), 2);
				btnSpace.setText(tempHeight);
				setSettings(TopConstants.BUTTON_SPACE, bar.getProgress(),
						TopConstants.INT);
			}
		});

		/*
		// 按键区透明度
		final TextView keysetsAlpha = (TextView) findViewById(R.id.keysetsAlpha);
		SeekBar keysetsAlphaBar = (SeekBar) findViewById(R.id.keysetsAlphaBar);
		keysetsAlphaBar.setMax(255);
		int tempKeysetsAlpha = prefer.getInt(TopConstants.KEY_SET_ALPHA, 123);
		keysetsAlphaBar.setProgress(tempKeysetsAlpha);
		keysetsAlpha.setText(String.valueOf(tempKeysetsAlpha));
		keysetsAlphaBar
				.setOnSeekBarChangeListener(new SeekbarSliderEndAdaptor() {
					@Override
					public void onProgressChanged(SeekBar bar, int arg1,
							boolean arg2) {
						int tempHeight = bar.getProgress();
						keysetsAlpha.setText(String.valueOf(tempHeight));
						setSettings(TopConstants.KEY_SET_ALPHA, tempHeight,
								TopConstants.INT);
					}
				});

		// 按键区背景色
		final TextView keysetsColor = (TextView) findViewById(R.id.keysetsColor);
		SeekBar keysetsColorBar = (SeekBar) findViewById(R.id.keysetsColorBar);
		keysetsColorBar.setMax(7);
		int tempKeysetsColor = prefer.getInt(TopConstants.KEY_SET_COLOR, 0);
		keysetsColorBar.setProgress(tempKeysetsColor);
		keysetsColor.setText(getRealValue(tempKeysetsColor, 3));
		keysetsColorBar
				.setOnSeekBarChangeListener(new SeekbarSliderEndAdaptor() {
					@Override
					public void onProgressChanged(SeekBar bar, int arg1,
							boolean arg2) {
						int tempHeight = bar.getProgress();
						keysetsColor.setText(getRealValue(tempHeight, 3));
						setSettings(TopConstants.KEY_SET_COLOR, tempHeight,
								TopConstants.INT);
					}
				});
		*/
	}

	/**
	 * <pre>
	 * 保存设置
	 * </pre>
	 */
	private void setSettings(String key, Object value, int type) {
		setSettings(key, value, false, -1, type);
	}

	/**
	 * <pre>
	 * 保存设置
	 * </pre>
	 */
	private void setSettings(String key, Object value, boolean isShowMsg,
			int msgId, int type) {
		Editor editor = prefer.edit();
		switch (type) {
		case TopConstants.INT:
			editor.putInt(key, Integer.valueOf(value.toString()));
			break;
		case TopConstants.STRING:
			editor.putString(key, value.toString());
			break;
		case TopConstants.BOOLEAN:
			editor.putBoolean(key, Boolean.valueOf(value.toString()));
			break;
		default:
			break;
		}
		editor.commit();
		if (isShowMsg) {
			Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 获取按键位置
	 * 
	 * @param location
	 * @return
	 */
	private String getLocation(int location) {
		String result = context.getString(R.string.buttomLayout);
		switch (location) {
		case TopConstants.BUTTOM:
			result = context.getString(R.string.buttomLayout);
			break;
		case TopConstants.LEFT:
			result = context.getString(R.string.leftLayout);
			break;
		case TopConstants.RIGHT:
			result = context.getString(R.string.rightLayout);
			break;
		default:
			result = context.getString(R.string.buttomLayout);
			break;
		}

		return result;
	}

	private String getRealValue(int height, int type) {
		String value = "";
		switch (type) {
		case 1:
			value = String.valueOf(height * 5 + 80);
			break;
		case 2:
			value = String.valueOf(height * 1 + 5);
			break;
		case 3:
			switch (height) {
			case 0:
				value = context.getString(R.string.blackColor);
				break;
			case 1:
				value = context.getString(R.string.redColor);
				break;
			case 2:
				value = context.getString(R.string.greenColor);
				break;
			case 3:
				value = context.getString(R.string.blueColor);
				break;
			case 4:
				value = context.getString(R.string.whiteColor);
				break;
			case 5:
				value = context.getString(R.string.yellowColor);
				break;
			case 6:
				value = context.getString(R.string.grayColor);
				break;
			case 7:
				value = context.getString(R.string.lightBlueColor);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

		return value;
	}

	/**
	 * 重新自定义按键
	 */
	private void addBtn() {
		Intent intent = new Intent(FullNexus4Activity.this,
				SetKeysActivity.class);
		startActivityForResult(intent, TopConstants.REQUEST_CODE);
	}

	/**
	 * 重启按键服务
	 */
	private void restartKeyService() {
		Intent intent = new Intent(context, FullNexus4Service.class);
		context.stopService(intent);
		context.startService(intent);
		Toast.makeText(context, R.string.keyServiceStarted, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 开启全屏
	 * 
	 * @param flag
	 */
	private void openFullScreen(boolean flag) {
		if (flag) {
			RunCommand.runSuCommandReturnBoolean(RunCommand.CmdMountSystemRW
					+ " chmod 666 /system/build.prop");
			BuildFileOperate.getInstance().appendLine(HIDE_NAV_CMD);
			RunCommand.runSuCommandReturnBoolean(RunCommand.CmdMountSystemRW
					+ " chmod 644 /system/build.prop");
			setSettings(TopConstants.OPEN_FULL_SCREEN, true,
					TopConstants.BOOLEAN);
			Toast.makeText(FullNexus4Activity.this, R.string.fullScreenStarted,
					Toast.LENGTH_SHORT).show();
		} else {
			RunCommand.runSuCommandReturnBoolean(RunCommand.CmdMountSystemRW
					+ " chmod 666 /system/build.prop");
			BuildFileOperate.getInstance().removeLine(HIDE_NAV_CMD);
			RunCommand.runSuCommandReturnBoolean(RunCommand.CmdMountSystemRW
					+ " chmod 644 /system/build.prop");
			setSettings(TopConstants.OPEN_FULL_SCREEN, false,
					TopConstants.BOOLEAN);
			Toast.makeText(FullNexus4Activity.this, R.string.fullScreenStopted,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 设置虚拟按钮的回调方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TopConstants.REQUEST_CODE
				&& resultCode == TopConstants.RESULT_CODE) {
			final ExtraImageView iv = new ExtraImageView(context);
			String keyName = data.getStringExtra(TopConstants.IS_V_KEY);
			// 基本按钮的处理方法
			if (null != keyName && keyName.startsWith(TopConstants.VKEY_PREFIX)) {
				int btnId = Integer.valueOf(keyName
						.substring(TopConstants.VKEY_PREFIX.length()));
				switch (btnId) {
				case ButtonFactory.BACK:
					iv.setImageResource((R.drawable.ic_back));
					break;
				case ButtonFactory.HOME:
					iv.setImageResource((R.drawable.ic_home));
					break;
				case ButtonFactory.RECENT:
					iv.setImageResource((R.drawable.ic_recent));
					break;
				case ButtonFactory.MENU:
					iv.setImageResource((R.drawable.ic_menu));
					break;
				case ButtonFactory.POWER:
					iv.setImageResource((R.drawable.ic_screen_off));
					break;
				case ButtonFactory.SEARCH:
					iv.setImageResource((R.drawable.ic_search));
					break;
				case ButtonFactory.HIDE:
					iv.setImageResource((R.drawable.ic_page_end));
					break;
				default:
					break;
				}
			}
			// 应用程序的处理方法
			else {
				Bundle bundle = data.getExtras();
				ApplicationInfo app = (ApplicationInfo) bundle
						.getParcelable(TopConstants.RESULT_DATA);
				iv.setImageDrawable(app.loadIcon(getPackageManager()));
				keyName = TopConstants.PKG_PREFIX + app.packageName;
			}

			iv.setPkgName(keyName);
			final String keyTemp = keyName;
			// 设置按钮长按事件，长按后删除此按钮
			iv.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View view) {
					keyTempPanel.removeView(iv);
					keys.remove(keyTemp);
					return true;
				}
			});

			lp.setMargins(0, 0, 10, 0);
			keyTempPanel.addView(iv, keyTempPanel.getChildCount() - 1, lp);
			keys.add(keyTemp);
		}
	}

	@Override
	protected void onDestroy() {
		BuildFileOperate.getInstance().destroy();
		keys.clear();
		keys = null;
		super.onDestroy();
		// restartKeyService();
	}

}
