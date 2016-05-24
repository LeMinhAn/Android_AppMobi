package vn.appsmobi.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import vn.appsmobi.R;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.model.DataCardItem;

/**
 * Created by tobrother on 04/01/2016.
 */
public class ViewHolderTextInfo extends RecyclerView.ViewHolder {
    private TextView tvTitleInfo;
    private TextView tvContentInfo;
    private TextView tvActionInfo;

    public ViewHolderTextInfo(View view, Context context, int scaleSize) {
        super(view);
        tvTitleInfo = (TextView) view.findViewById(R.id.tvTitleInfo);
        tvContentInfo = (TextView) view.findViewById(R.id.tvContentInfo);
        tvActionInfo = (TextView) view.findViewById(R.id.tvActionInfo);
    }

    public void setValue(CardItem cardItem, Context context) {
        DataCardItem item = new DataCardItem();
        try {
            item.valueOf(cardItem.getCard_data().getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getTvTitleInfo().setText(item.getName());
        getTvContentInfo().setText(item.getContent());
        getTvActionInfo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public TextView getTvTitleInfo() {
        return tvTitleInfo;
    }

    public void setTvTitleInfo(TextView tvTitleInfo) {
        this.tvTitleInfo = tvTitleInfo;
    }

    public TextView getTvContentInfo() {
        return tvContentInfo;
    }

    public void setTvContentInfo(TextView tvContentInfo) {
        this.tvContentInfo = tvContentInfo;
    }

    public TextView getTvActionInfo() {
        return tvActionInfo;
    }

    public void setTvActionInfo(TextView tvActionInfo) {
        this.tvActionInfo = tvActionInfo;
    }
}
