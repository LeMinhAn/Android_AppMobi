package vn.appsmobi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.appsmobi.R;
import vn.appsmobi.model.DataCardItem;

/**
 * Created by Le Minh An on 5/24/2016.
 */
public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MyViewHolder> {

    private List<DataCardItem> dataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }


    public MoreAdapter(List<DataCardItem> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.more_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataCardItem more = dataList.get(position);
        holder.title.setText(more.getName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
