package com.example.vuong.wifiscanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vuong.wifiscanner.R;
import com.example.vuong.wifiscanner.objects.Wifi;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by vuong on 22/01/2018.
 */

public class ListViewAdapter extends BaseAdapter {

    private List<Wifi> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListViewAdapter(Context aContext, List<Wifi> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);

            holder.name = (TextView) convertView.findViewById(R.id.txtName);
            holder.ssid = (TextView) convertView.findViewById(R.id.txtSSID);

            Wifi wifi = this.listData.get(position);

            holder.name.setText(wifi.getName());
            holder.ssid.setText(wifi.getSsid());

            if(wifi.isConnected()) {
                convertView.setBackgroundColor(Color.parseColor("#00ff00"));
            } else {
                convertView.setBackgroundColor(Color.parseColor("#f0f0f0"));
            }

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

            Wifi wifi = this.listData.get(position);

            holder.name.setText(wifi.getName());
            holder.ssid.setText(wifi.getSsid());

            if(wifi.isConnected()) {
                convertView.setBackgroundColor(Color.parseColor("#00ff00"));
            } else {
                convertView.setBackgroundColor(Color.parseColor("#f0f0f0"));
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView ssid;
    }
}
