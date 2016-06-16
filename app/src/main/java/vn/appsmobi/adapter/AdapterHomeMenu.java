package vn.appsmobi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.appsmobi.R;
import vn.appsmobi.model.ItemMenu;
import vn.appsmobi.utils.Constants;
import vn.appsmobi.utils.SizeCalculator;
import vn.appsmobi.utils.UIUtils;

import static vn.appsmobi.utils.UIUtils.setViewSize;

public class AdapterHomeMenu extends BaseAdapter {

    private Context context;
    private ArrayList<ItemMenu> item_menus;
    private int sizeIconMenu;
    private ViewHolder holder;

    public AdapterHomeMenu(Context context, ArrayList<ItemMenu> item_menus, int sizeIconMenu) {
        this.context = context;
        this.item_menus = item_menus;
        this.sizeIconMenu = sizeIconMenu;
    }

    @Override
    public int getCount() {
        return item_menus.size();
    }

    @Override
    public Object getItem(int position) {
        return item_menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater li = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_menu, null);
            holder.tvNameMenu = (TextView) convertView.findViewById(R.id.tvNameMenu);
            holder.ivImageMenu = (ImageView) convertView.findViewById(R.id.ivImageMenu);
            setViewSize(holder.ivImageMenu, sizeIconMenu, sizeIconMenu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNameMenu.setText(item_menus.get(position).getName_menu());
        holder.ivImageMenu.setLayoutParams(SizeCalculator.getManager().lpIconMenu);
        int i = item_menus.get(position).getImage_menu();
        String imageUri = "drawable://" + item_menus.get(position).getImage_menu();
        UIUtils.loadImageLoader(Constants.options, imageUri, holder.ivImageMenu);
        //sizeIconMenu = 250;
        // holder.ivImageMenu.setImageDrawable(scaleImageOf(i,sizeIconMenu,sizeIconMenu,context));
        return convertView;
    }

    private class ViewHolder {
        protected TextView tvNameMenu;
        protected ImageView ivImageMenu;
    }
}
