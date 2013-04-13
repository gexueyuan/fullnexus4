/**
 * <pre>
 * Title: 		TopService.java
 * Project: 	TopService
 * Type:		com.leoly.TopService
 * Author:		255507
 * Create:	 	2013-1-8 下午6:37:44
 * Copyright: 	Copyright (c) 2013
 * Company:		
 * <pre>
 */
package com.leoly.fullnexus4.services;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.leoly.fullnexus4.R;
import com.leoly.fullnexus4.adaptors.GestureAdaptor;
import com.leoly.fullnexus4.utils.ButtonFactory;
import com.leoly.fullnexus4.utils.TopConstants;
import com.leoly.fullnexus4.views.ExtraImageView;
import com.leoly.fullnexus4.views.ExtraScrollView;
import com.leoly.fullnexus4.views.IScrollView;

/**
 * <pre>
 * </pre>
 * 
 * @author 255507
 * @version 1.0, 2013-1-8
 */
public class FullNexus4Service extends Service {

	private WindowManager wm;

	private LinearLayout layout;

	private WindowManager.LayoutParams params = null;

	private Context context;

	private ImageView tab;

	// 按键区当前状态是显示还是隐藏
	private boolean isShow = true;

	// 按键区位置
	private int keyLocation = TopConstants.BUTTOM;

	// 按键区的高度,在左边和右边时为宽度
	private int keyHeight = 130;

	// 按钮间隔,左右都有间隔
	private int btnSpace = 5;

	// 按键区隐藏时间
	private int hideTime = 5;

	// 按钮高度
	private int btnHeight = 30;

	// 按键区域透明度
	private int keyAlpha = 123;

	// 按键区域背景色
	private int keysetColor = Color.BLACK;

	private GestureDetector detector = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			boolean wantShow = data.getBoolean(TopConstants.HANDLER_TAG);
			if (wantShow) {
				if (isShow) {
					hideKeys();
				} else {
					showKeys();
				}
			}
		}
	};

	private Runnable run = new Runnable() {
		public void run() {
			Bundle bundle = new Bundle();
			bundle.putBoolean(TopConstants.HANDLER_TAG, true);
			Message msg = new Message();
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	};

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		context = getApplicationContext();

		detector = new GestureDetector(FullNexus4Service.this,
				new GestureAdaptor() {
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						if (keyLocation == TopConstants.BUTTOM) {
							if (e1.getY() > e2.getY()) {
								showKeys();
							}
						} else if (keyLocation == TopConstants.LEFT) {
							if (e1.getX() < e2.getX()) {
								showKeys();
							}
						} else {
							if (e1.getX() > e2.getX()) {
								showKeys();
							}
						}
						return false;
					};
				});

		// 顶级窗口配置
		wm = (WindowManager) getApplicationContext().getSystemService(
				WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();
		// 关键属性
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		params.format = 1;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		params.flags = params.flags
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		params.flags = params.flags
				| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		isShow = true;

		// 屏幕大小
		Display display = wm.getDefaultDisplay();
		// 加载Layout
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// 加载配置项
		SharedPreferences prefer = getSharedPreferences(
				TopConstants.CONFIG_NAME, MODE_PRIVATE);
		// 获取配置的按键位置
		keyLocation = prefer.getInt(TopConstants.KEY_LOCATION,
				TopConstants.BUTTOM);
		// 基本信息
		keyHeight = prefer.getInt(TopConstants.KEY_SET_HEIGHT, 10);
		keyHeight = ButtonFactory.getRealValue(keyHeight, 1);
		btnSpace = prefer.getInt(TopConstants.BUTTON_SPACE, 5);
		btnSpace = ButtonFactory.getRealValue(btnSpace, 2);
		keyAlpha = prefer.getInt(TopConstants.KEY_SET_ALPHA, 123);
		keysetColor = prefer.getInt(TopConstants.KEY_SET_COLOR, Color.BLACK);
		// 获取配置的按键
		String customKeys = prefer.getString(TopConstants.CUSTOM_KEYS, null);
		// 设置自动隐藏时间
		hideTime = prefer.getInt(TopConstants.HIDE_TIME, 5);
		// 根据按键配置获取真实的按钮
		ButtonFactory facotry = ButtonFactory.getInstance(context, keyLocation,
				new ICallBack() {
					public void run() {
						handleTab(TopConstants.REMOVE_AND_POST);
					}

					public void hideAllKeys() {
						hideKeys();
					}
				});
		List<ExtraImageView> btns = facotry.getButtons(customKeys);

		// 设置总体高度，如果按键高度超过横屏宽度，则高度为手机横屏宽度
		int height = display.getWidth();
		// 按键区应该占有的宽度:按钮个数 - 按钮高度(按钮高度和宽度是一样的) + 按钮间隔(左右间隔)
		int keyWidth = keyHeight - btnHeight + btnSpace * 2;
		height = btns.size() * keyWidth > height ? height : btns.size()
				* keyWidth;

		LayoutParams btnParam = new LayoutParams(keyHeight - btnHeight,
				keyHeight - btnHeight);

		switch (keyLocation) {
		case TopConstants.BUTTOM:
			params.width = height;
			params.height = keyHeight;
			params.y = 0;
			params.gravity = Gravity.BOTTOM;
			layout = (LinearLayout) inflater.inflate(R.layout.main, null);
			btnParam.setMargins(btnSpace, 0, btnSpace, 0);
			break;
		case TopConstants.LEFT:
			params.width = keyHeight;
			params.height = height;
			params.x = 0;
			params.gravity = Gravity.LEFT;
			layout = (LinearLayout) inflater.inflate(R.layout.main_left, null);
			btnParam.setMargins(0, btnSpace, 0, btnSpace);
			break;
		case TopConstants.RIGHT:
			params.width = keyHeight;
			params.height = height;
			params.x = 0;
			params.gravity = Gravity.RIGHT;
			layout = (LinearLayout) inflater.inflate(R.layout.main_right, null);
			btnParam.setMargins(0, btnSpace, 0, btnSpace);
			break;
		default:
			break;
		}

		LinearLayout btnLayout = (LinearLayout) layout
				.findViewById(R.id.btnsLayout);
		boolean hasHideKey = false;
		String hideKeyPkgName = TopConstants.VKEY_PREFIX + ButtonFactory.HIDE;
		for (ExtraImageView iv : btns) {
			if (hideKeyPkgName.equals(iv.getPkgName())) {
				hasHideKey = true;
			}

			btnLayout.addView(iv, btnLayout.getChildCount(), btnParam);
		}

		final boolean hhk = hasHideKey;

		layout.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent event) {
				if (!hhk && event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					hideKeys();
				}

				return false;
			}
		});
		tab = (ImageView) layout.findViewById(R.id.tabPanel);
		tab.setLongClickable(true);
		tab.setAlpha(0.3f);
		tab.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent event) {
				return detector.onTouchEvent(event);
			}
		});

		IScrollView esv = (IScrollView) layout
				.findViewById(R.id.extraScrollView);

		if (keyLocation == TopConstants.LEFT) {
			ExtraScrollView es = (ExtraScrollView) esv;
			android.view.ViewGroup.LayoutParams param = es.getLayoutParams();
			param.width = keyHeight - 25;
			es.setLayoutParams(param);
		}

		// esv.setColor(ButtonFactory.getRealColor(keysetColor));
		// esv.setBackgroundAlpha(keyAlpha);
		wm.addView(layout, params);
		handleTab(TopConstants.POST);
	}

	private void hideKeys() {
		if (!isShow) {
			return;
		}
		switch (keyLocation) {
		case TopConstants.BUTTOM:
			params.y = -(keyHeight - 23);
			break;
		case TopConstants.LEFT:
			params.x = -(keyHeight - 25);
			break;
		case TopConstants.RIGHT:
			params.x = -(keyHeight - 23);
			break;
		default:
			break;
		}

		wm.updateViewLayout(layout, params);
		isShow = false;
		handleTab(TopConstants.REMOVE);
	}

	private void showKeys() {
		if (isShow) {
			return;
		}
		switch (keyLocation) {
		case TopConstants.BUTTOM:
			params.y = 0;
			break;
		case TopConstants.LEFT:
			params.x = 0;
			break;
		case TopConstants.RIGHT:
			params.x = 0;
			break;
		default:
			break;
		}

		wm.updateViewLayout(layout, params);
		isShow = true;
		handleTab(TopConstants.POST);
	}

	private void handleTab(int type) {
		if (hideTime > 0) {
			switch (type) {
			case TopConstants.POST:
				handler.postDelayed(run, hideTime * 1000);
				break;
			case TopConstants.REMOVE:
				handler.removeCallbacks(run);
				break;
			case TopConstants.REMOVE_AND_POST:
				handler.removeCallbacks(run);
				handler.postDelayed(run, hideTime * 1000);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		wm.removeViewImmediate(layout);
		handleTab(TopConstants.REMOVE);
		stopSelf();
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
	}
}
