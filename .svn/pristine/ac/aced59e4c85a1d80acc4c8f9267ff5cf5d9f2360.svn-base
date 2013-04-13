/**
 * 
 */
package com.leoly.fullnexus4.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.leoly.fullnexus4.services.ICallBack;

/**
 * @author culin003
 */
public class ExtraHorizontalScrollView extends HorizontalScrollView implements IScrollView
{
	private ICallBack callback;

	public ExtraHorizontalScrollView(Context context)
	{
		super(context);
	}

	public ExtraHorizontalScrollView(Context context, AttributeSet attr)
	{
		super(context, attr);
	}

	public ExtraHorizontalScrollView(Context context, AttributeSet attr, int defType)
	{
		super(context, attr, defType);
	}

	
	@Override
	public void fling(int velocityX)
	{
		if (null != callback)
		{
			callback.run();
		}
		super.fling(velocityX);
	}

	public void setCallback(ICallBack callback)
	{
		this.callback = callback;
	}

	public void setBackgroundAlpha(int alpha)
	{
		super.getBackground().setAlpha(alpha);
	}

	public void setColor(int color)
	{
		super.setBackgroundColor(color);
	}
}
