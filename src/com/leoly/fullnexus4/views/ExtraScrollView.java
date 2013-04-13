/**
 * 
 */
package com.leoly.fullnexus4.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.leoly.fullnexus4.services.ICallBack;

/**
 * @author culin003
 */
public class ExtraScrollView extends ScrollView implements IScrollView
{

	private ICallBack callback;

	public ExtraScrollView(Context context)
	{
		super(context);
	}

	public ExtraScrollView(Context context, AttributeSet attr)
	{
		super(context, attr);
	}

	public ExtraScrollView(Context context, AttributeSet attr, int defType)
	{
		super(context, attr, defType);
	}

	@Override
	public void fling(int velocityY)
	{
		if (null != callback)
		{
			callback.run();
		}
		super.fling(velocityY);
	}

	public void setCallback(ICallBack callback)
	{
		this.callback = callback;
	}

	public void setBackgroundAlpha(int alpha)
	{
		getBackground().setAlpha(alpha);
	}

	public void setColor(int color)
	{
		super.setBackgroundColor(color);
	}
}
