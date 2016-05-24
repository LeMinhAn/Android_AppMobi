package vn.appsmobi.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import vn.appsmobi.R;
import vn.appsmobi.model.CardItem;
import vn.appsmobi.model.CategoryAdvanceItem;

/**
 * Created by tobrother on 30/12/2015.
 */
public class ViewHolderCategory extends RecyclerView.ViewHolder {
    private TextView tvCategoryName;

    public ViewHolderCategory(View itemView) {
        super(itemView);
        tvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
        tvCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setValue(CardItem cardItem) {
        CategoryAdvanceItem item = new CategoryAdvanceItem();
        try {
            item.valueOf(cardItem.getCard_data().getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getTvCategoryName().setText(item.getName());
    }

    public TextView getTvCategoryName() {
        return tvCategoryName;
    }

    public void setTvCategoryName(TextView tvCategoryName) {
        this.tvCategoryName = tvCategoryName;
    }
}
