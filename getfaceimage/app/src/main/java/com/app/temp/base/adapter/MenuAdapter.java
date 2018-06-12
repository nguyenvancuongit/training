package com.app.temp.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.temp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * adapter for menu
 */
public class MenuAdapter extends BaseAdapter {
    List<String> menuList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public MenuAdapter(Context context, List<String> menuList) {
        this.menuList = menuList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public String getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_menu, parent, false);
            mViewHolder = new MenuViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MenuViewHolder) convertView.getTag();
        }
        String currentListData = getItem(position);

        mViewHolder.tvTitle.setText(currentListData);

        return convertView;
    }

    private class MenuViewHolder {
        TextView tvTitle;

        public MenuViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.tv_title);
        }
    }

    public void setMenuList(List<String> menuList) {
        this.menuList = menuList;
    }
}
