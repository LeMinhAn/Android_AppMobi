package vn.appsmobi.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import vn.appsmobi.R;

import static vn.appsmobi.utils.UIUtils.setViewSize;

/**
 * Created by tobrother on 05/01/2016.
 */
public class ViewHolderItemAppManager extends RecyclerView.ViewHolder {
    private ImageView ivItemImageAppManage;
    private TextView tvItemNameAppManage;
    private TextView tvItemAuthorAppManage;
    private TextView tvItemStatusAppManage;
    private RelativeLayout rlMenuAppManage;
    private RelativeLayout rlProcessAppManage;
    private ImageView ivStopProcessAppManager;
    private ProgressBar pbProcessAppManage;
    private TextView tvItemProcessAppManage;
    private TextView tvItemPercentProcessAppManage;

    public ViewHolderItemAppManager(View viewParent, int size) {
        super(viewParent);
        ivItemImageAppManage = (ImageView) viewParent.findViewById(R.id.ivItemImageAppManage);
        tvItemNameAppManage = (TextView) viewParent.findViewById(R.id.tvItemNameAppManage);
        tvItemAuthorAppManage = (TextView) viewParent.findViewById(R.id.tvItemAuthorAppManage);
        tvItemStatusAppManage = (TextView) viewParent.findViewById(R.id.tvItemStatusAppManage);
        rlMenuAppManage = (RelativeLayout) viewParent.findViewById(R.id.rlMenuAppManage);
        rlProcessAppManage = (RelativeLayout) viewParent.findViewById(R.id.rlProcessAppManage);
        ivStopProcessAppManager = (ImageView) viewParent.findViewById(R.id.ivStopProcessAppManager);
        pbProcessAppManage = (ProgressBar) viewParent.findViewById(R.id.pbProcessAppManage);
        tvItemProcessAppManage = (TextView) viewParent.findViewById(R.id.tvItemProcessAppManage);
        tvItemPercentProcessAppManage = (TextView) viewParent.findViewById(R.id.tvItemPercentProcessAppManage);
        setViewSize(ivItemImageAppManage, size, size);
    }

    public ImageView getIvItemImageAppManage() {
        return ivItemImageAppManage;
    }

    public void setIvItemImageAppManage(ImageView ivItemImageAppManage) {
        this.ivItemImageAppManage = ivItemImageAppManage;
    }

    public TextView getTvItemNameAppManage() {
        return tvItemNameAppManage;
    }

    public void setTvItemNameAppManage(TextView tvItemNameAppManage) {
        this.tvItemNameAppManage = tvItemNameAppManage;
    }

    public TextView getTvItemAuthorAppManage() {
        return tvItemAuthorAppManage;
    }

    public void setTvItemAuthorAppManage(TextView tvItemAuthorAppManage) {
        this.tvItemAuthorAppManage = tvItemAuthorAppManage;
    }

    public TextView getTvItemStatusAppManage() {
        return tvItemStatusAppManage;
    }

    public void setTvItemStatusAppManage(TextView tvItemStatusAppManage) {
        this.tvItemStatusAppManage = tvItemStatusAppManage;
    }

    public RelativeLayout getRlMenuAppManage() {
        return rlMenuAppManage;
    }

    public void setRlMenuAppManage(RelativeLayout rlMenuAppManage) {
        this.rlMenuAppManage = rlMenuAppManage;
    }

    public RelativeLayout getRlProcessAppManage() {
        return rlProcessAppManage;
    }

    public void setRlProcessAppManage(RelativeLayout rlProcessAppManage) {
        this.rlProcessAppManage = rlProcessAppManage;
    }

    public ImageView getIvStopProcessAppManager() {
        return ivStopProcessAppManager;
    }

    public void setIvStopProcessAppManager(ImageView ivStopProcessAppManager) {
        this.ivStopProcessAppManager = ivStopProcessAppManager;
    }

    public ProgressBar getPbProcessAppManage() {
        return pbProcessAppManage;
    }

    public void setPbProcessAppManage(ProgressBar pbProcessAppManage) {
        this.pbProcessAppManage = pbProcessAppManage;
    }

    public TextView getTvItemProcessAppManage() {
        return tvItemProcessAppManage;
    }

    public void setTvItemProcessAppManage(TextView tvItemProcessAppManage) {
        this.tvItemProcessAppManage = tvItemProcessAppManage;
    }

    public TextView getTvItemPercentProcessAppManage() {
        return tvItemPercentProcessAppManage;
    }

    public void setTvItemPercentProcessAppManage(TextView tvItemPercentProcessAppManage) {
        this.tvItemPercentProcessAppManage = tvItemPercentProcessAppManage;
    }
}
