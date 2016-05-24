package vn.appsmobi.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vn.appsmobi.R;

/**
 * Created by tobrother on 05/01/2016.
 */
public class ViewHolderSectionAppManage extends RecyclerView.ViewHolder {
    private TextView tvSectionAppManage;
    private Button btSectionAppManage;

    public ViewHolderSectionAppManage(View itemView) {
        super(itemView);
        tvSectionAppManage = (TextView) itemView.findViewById(R.id.tvSectionAppManage);
        btSectionAppManage = (Button) itemView.findViewById(R.id.btSectionAppManage);
    }

    public TextView getTvSectionAppManage() {
        return tvSectionAppManage;
    }

    public void setTvSectionAppManage(TextView tvSectionAppManage) {
        this.tvSectionAppManage = tvSectionAppManage;
    }

    public Button getBtSectionAppManage() {
        return btSectionAppManage;
    }

    public void setBtSectionAppManage(Button btSectionAppManage) {
        this.btSectionAppManage = btSectionAppManage;
    }
}
