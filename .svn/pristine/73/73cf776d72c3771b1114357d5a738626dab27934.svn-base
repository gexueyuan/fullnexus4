/**
 * 
 */
package com.leoly.fullnexus4.adaptors;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leoly.fullnexus4.R;
import com.leoly.fullnexus4.vos.AppObject;

/**
 * @author culin003
 */
public class AppListAdapter extends BaseAdapter
{
	private Context context;

	private List<AppObject> data;

	public AppListAdapter(Context context, List<AppObject> data)
	{
		this.context = context;
		this.data = data;
	}

	public int getCount()
	{
		return data.size();
	}

	public Object getItem(int position)
	{
		return data.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
		}
		ImageView icon = (ImageView) convertView.findViewById(R.id.appImage);
		TextView name = (TextView) convertView.findViewById(R.id.appPkgName);
		icon.setImageDrawable(data.get(position).getIcon());
		name.setText(data.get(position).getName());
		return convertView;
	}
}
