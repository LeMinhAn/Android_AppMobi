package vn.appsmobi.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import vn.appsmobi.R;

import static vn.appsmobi.utils.UIUtils.getScreenWidth;
import static vn.appsmobi.utils.UIUtils.setViewSize;

/**
 * Created by tobrother on 04/01/2016.
 */
public class ViewHolderComment extends RecyclerView.ViewHolder {
    private TextView tvNameUserComment;
    private TextView tvIntroComment;
    private TextView tvTimeUserComment;
    private RatingBar rbRatingComment;
    private RoundedImageView ivAvatarComment;

    public ViewHolderComment(View view, Context context, int scaleSize) {
        super(view);
        tvNameUserComment = (TextView) view.findViewById(R.id.tvNameUserComment);
        tvTimeUserComment = (TextView) view.findViewById(R.id.tvTimeUserComment);
        tvIntroComment = (TextView) view.findViewById(R.id.tvIntroComment);
        rbRatingComment = (RatingBar) view.findViewById(R.id.rbRatingComment);
        ivAvatarComment = (RoundedImageView) view.findViewById(R.id.ivAvatarComment);
        int sizeImage = getScreenWidth(context) / scaleSize;
        setViewSize(ivAvatarComment, sizeImage, sizeImage);
    }


    public TextView getTvNameUserComment() {
        return tvNameUserComment;
    }

    public void setTvNameUserComment(TextView tvNameUserComment) {
        this.tvNameUserComment = tvNameUserComment;
    }

    public TextView getTvIntroComment() {
        return tvIntroComment;
    }

    public void setTvIntroComment(TextView tvIntroComment) {
        this.tvIntroComment = tvIntroComment;
    }

    public TextView getTvTimeUserComment() {
        return tvTimeUserComment;
    }

    public void setTvTimeUserComment(TextView tvTimeUserComment) {
        this.tvTimeUserComment = tvTimeUserComment;
    }

    public RatingBar getRbRatingComment() {
        return rbRatingComment;
    }

    public void setRbRatingComment(RatingBar rbRatingComment) {
        this.rbRatingComment = rbRatingComment;
    }

    public RoundedImageView getIvAvatarComment() {
        return ivAvatarComment;
    }

    public void setIvAvatarComment(RoundedImageView ivAvatarComment) {
        this.ivAvatarComment = ivAvatarComment;
    }
}
