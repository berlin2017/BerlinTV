package com.berlin.berlintv;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by berlin on 2016/7/14 0014.
 */
public class MyAdapter extends BaseAdapter{

    private Context context;
    private List<TVBean>list;

    public MyAdapter(Context context, List<TVBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tv_list,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_tv_img);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_tv_name);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textView.setText(list.get(position).getName());
        if (list.get(position).getImgurl()!=null){
            ImageLoadUtil imageLoadUtil = new ImageLoadUtil(context);
            imageLoadUtil.display(viewHolder.imageView,list.get(position).getImgurl());
        }
        return convertView;
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }
}
