package com.leoly.fullnexus4.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.leoly.fullnexus4.R;
import com.leoly.fullnexus4.adaptors.AppListAdapter;
import com.leoly.fullnexus4.utils.ButtonFactory;
import com.leoly.fullnexus4.utils.TopConstants;
import com.leoly.fullnexus4.vos.AppObject;

public class SetKeysActivity extends Activity
{
	// 列表数据与视图的适配器
	private AppListAdapter adapter;

	private PackageManager manager;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTitle(R.string.selectApps);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keys);
		manager = getPackageManager();
		List<ApplicationInfo> apps = manager.getInstalledApplications(PackageManager.PERMISSION_GRANTED);
		List<AppObject> data = new ArrayList<AppObject>();
		data.addAll(ButtonFactory.getSysButtons(SetKeysActivity.this));
		AppObject obj = null;
		for (ApplicationInfo info : apps)
		{
			// if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
			// {
			obj = new AppObject();
			obj.setIcon(info.loadIcon(manager));
			obj.setName(info.loadLabel(manager).toString());
			obj.setPackageName(info.packageName);
			obj.setInfo(info);
			data.add(obj);
			// }
		}
		adapter = new AppListAdapter(SetKeysActivity.this, data);
		ListView appList = (ListView) findViewById(R.id.appList);
		appList.setAdapter(adapter);
		appList.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> adapterView, View view, int index, long id)
			{
				AppObject ao = (AppObject) adapterView.getAdapter().getItem(index);
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putParcelable(TopConstants.RESULT_DATA, ao.getInfo());
				intent.putExtras(bundle);
				intent.putExtra(TopConstants.IS_V_KEY, ao.getPackageName());
				setResult(TopConstants.RESULT_CODE, intent);
				finish();
			}
		});
	}
}
