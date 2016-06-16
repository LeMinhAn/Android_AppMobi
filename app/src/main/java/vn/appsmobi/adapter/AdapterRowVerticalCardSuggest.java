package vn.appsmobi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import vn.appsmobi.R;
import vn.appsmobi.ui.ViewHolderVerticalCard;

public class AdapterRowVerticalCardSuggest extends BaseAdapter {
    private Context context;
    float scaleItem;
    private int cardDataType = 0;
    private JSONArray dataCardItems;
    private ViewHolderVerticalCard holder;

    public AdapterRowVerticalCardSuggest(Context context, JSONArray dataCardItems, float scaleItem, int cardDataType) {
        this.context = context;
        this.dataCardItems = dataCardItems;
        this.scaleItem = scaleItem;
        this.cardDataType = cardDataType;
    }

    @Override
    public int getCount() {
        return dataCardItems.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return dataCardItems.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_vertical_card, null);
            holder = new ViewHolderVerticalCard(convertView, context);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolderVerticalCard) convertView.getTag();
        }
        try {
            holder.setValue(context, dataCardItems.getJSONObject(position), cardDataType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
