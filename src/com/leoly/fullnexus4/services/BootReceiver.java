package com.leoly.fullnexus4.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.leoly.fullnexus4.R;
import com.leoly.fullnexus4.utils.TopConstants;

public class BootReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent i)
	{
		SharedPreferences prefer = context.getSharedPreferences(TopConstants.CONFIG_NAME, Context.MODE_PRIVATE);
		if (prefer.getBoolean(TopConstants.OPEN_KEY_SERVICE, false))
		{
			Intent intent = new Intent(context, FullNexus4Service.class);
			context.startService(intent);
			Toast.makeText(context, context.getString(R.string.keyServiceStarted), Toast.LENGTH_SHORT).show();
		}
	}

}
