package vn.appsmobi.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tobrother on 21/01/2016.
 */
public class CommentItem extends DataCardItem implements Parcelable {
    private String avatar_image;
    private String comment;
    private String post_time;
    private float rate;

    public CommentItem() {
        super();
    }

    public CommentItem(DataCardItem dataCardItem) {
        super(dataCardItem);
    }

    protected CommentItem(Parcel in) {
        super(in);
        avatar_image = in.readString();
        comment = in.readString();
        post_time = in.readString();
        rate = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(avatar_image);
        dest.writeString(comment);
        dest.writeString(post_time);
        dest.writeFloat(rate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
        @Override
        public CommentItem createFromParcel(Parcel in) {
            return new CommentItem(in);
        }

        @Override
        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

    public void valueOf(JSONObject jsonObject) throws JSONException {
        super.valueOf(jsonObject);
        if (!jsonObject.isNull("avatar_image")) {
            this.avatar_image = jsonObject.getString("avatar_image");
        }
        if (!jsonObject.isNull("comment")) {
            this.comment = jsonObject.getString("comment");
        }
        if (!jsonObject.isNull("post_time")) {
            this.post_time = jsonObject.getString("post_time");
        }
        if (!jsonObject.isNull("rate")) {
            this.rate = (float) jsonObject.getDouble("rate");
        }
    }


    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
