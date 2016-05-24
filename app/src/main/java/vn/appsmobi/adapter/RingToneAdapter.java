package vn.appsmobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;

import vn.appsmobi.R;
import vn.appsmobi.ui.ViewHolderRingStone;

/**
 * Created by tobrother on 27/01/2016.
 */
public class RingToneAdapter extends BaseAdapter {
    JSONArray dataCardItems;
    Context context;
    DisplayImageOptions options;
    int cardDataType;

    public RingToneAdapter(JSONArray dataCardItems, Context context, DisplayImageOptions options, int cardDataType) {
        this.dataCardItems = dataCardItems;
        this.context = context;
        this.options = options;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderRingStone viewHolderRingStone;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_ring_stone, null);
            viewHolderRingStone = new ViewHolderRingStone(convertView);
            viewHolderRingStone.getSizeHalf();
            viewHolderRingStone.setSize();
            // cant play ringtone in home activity
            viewHolderRingStone.getIvPlayRingStone().setVisibility(View.GONE);
            viewHolderRingStone.getCpcPlayRingStone().setVisibility(View.GONE);
            convertView.setTag(viewHolderRingStone);
        } else {
            viewHolderRingStone = (ViewHolderRingStone) convertView.getTag();
        }
        // set image size
        // setImageViewWithScale(viewHolderRingStone.getIvThumbRingStone(), viewHolderRingStone.getMyImageSize(), viewHolderRingStone.getMyImageSize(), dataCardItems.get(position).getIcon_image(), options);
        try {
            viewHolderRingStone.setValue(dataCardItems.getJSONObject(position), options, context, cardDataType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
