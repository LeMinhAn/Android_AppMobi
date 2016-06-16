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
import vn.appsmobi.ui.ViewHolderImage;

public class SlideImageAdapter extends BaseAdapter {

    private JSONArray dataCardItems;
    private Context context;
    private DisplayImageOptions options;
    private int cardDataType;
    private ViewHolderImage viewHolderImage;

    public SlideImageAdapter(JSONArray dataCardItems, Context context, DisplayImageOptions options, int cardDataType) {
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
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_card_image, null);
            viewHolderImage = new ViewHolderImage(convertView, context, 3);
            convertView.setTag(viewHolderImage);
        } else {
            viewHolderImage = (ViewHolderImage) convertView.getTag();
        }
        // set image size
        //setImageViewWithScale( viewHolderImage.getIvImageWallPaper(), viewHolderImage.getImageSize(), viewHolderImage.getImageSize(), dataCardItems.get(position).getIcon_image(), options);

        try {
            viewHolderImage.setValue(dataCardItems.getJSONObject(position), options, context, cardDataType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //ImageLoader.getInstance().displayImage(dataCardItems.get(position).getIcon_image(), viewHolderImage.getIvImageWallPaper(),options);
        return convertView;
    }

}
