package vn.appsmobi.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.appsmobi.utils.Constants;

/**
 * Created by tobrother on 19/02/2016.
 */
public class ListVersion implements Parcelable {
    private String title;
    private String link;

    public ListVersion(String title, String link) {
        this.title = title;
        this.link = link;
    }

    protected ListVersion(Parcel in) {
        title = in.readString();
        link = in.readString();
    }

    public ListVersion() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static final Creator<ListVersion> CREATOR = new Creator<ListVersion>() {
        @Override
        public ListVersion createFromParcel(Parcel in) {
            return new ListVersion(in);
        }

        @Override
        public ListVersion[] newArray(int size) {
            return new ListVersion[size];
        }
    };

    public static ArrayList<ListVersion> ValueOfList(JSONArray jsonArray) throws JSONException {
        JSONArray array = jsonArray;
        ArrayList<ListVersion> listVersions = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            listVersions.add(ValueOf(array.getJSONObject(i)));
        }
        return listVersions;
    }

    public static ListVersion ValueOf(JSONObject jsonObject) throws JSONException {
        ListVersion linkVersion = new ListVersion();
        if (jsonObject.isNull(Constants.JSON_PARSE.LINK_TITLE)) {
            linkVersion.setTitle(jsonObject.getString(Constants.JSON_PARSE.LINK_TITLE));
        }
        if (jsonObject.isNull(Constants.JSON_PARSE.LINK_DOWN)) {
            linkVersion.setLink(jsonObject.getString(Constants.JSON_PARSE.LINK_DOWN));
        }
        return linkVersion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
    }
}
