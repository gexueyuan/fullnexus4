/**
 * 
 */
package com.leoly.fullnexus4.views;

import com.leoly.fullnexus4.services.ICallBack;

/**
 * @author culin003
 */
public interface IScrollView
{
	void setCallback(ICallBack callBack);

	void setBackgroundAlpha(int alpha);

	void setColor(int color);
}
