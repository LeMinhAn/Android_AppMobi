package vn.appsmobi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.appsmobi.R;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.ui.ViewHolderVerticalCard;

/**
 * Created by tobrother on 05/01/2016.
 */
public class AppManageAdapter extends RecyclerView.Adapter<ViewHolderVerticalCard> {
    private Context context;
    private ArrayList<DataCardItem> itemAppManages;

    public AppManageAdapter(Context context, ArrayList<DataCardItem> itemAppManages) {
        this.context = context;
        this.itemAppManages = itemAppManages;
    }

    @Override
    public ViewHolderVerticalCard onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vertical_card, parent, false);
        return new ViewHolderVerticalCard(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolderVerticalCard holder, int position) {
        DataCardItem item = itemAppManages.get(position);
        holder.setValue(context, item);
    }

    @Override
    public int getItemCount() {
        return itemAppManages.size();
    }
}
