package vn.appsmobi.model;

import org.json.JSONException;
import org.json.JSONObject;

import vn.appsmobi.utils.Constants;

/**
 * Created by tobrother on 21/01/2016.
 */
public class WallPaperItem extends DataCardItem {
    private String file_style;

    public WallPaperItem(DataCardItem dataCardItem) {
        super(dataCardItem);
    }

    public WallPaperItem() {
        super();
    }

    public void valueOf(JSONObject jsonObject) throws JSONException {
        super.valueOf(jsonObject);
        if (!jsonObject.isNull(Constants.JSON_PARSE.FILE_TYPE)) {
            this.setFile_style(jsonObject.getString(Constants.JSON_PARSE.FILE_TYPE));
        }
    }

    /*
    public ArrayList<WallPaperItem> valueOfList(JSONArray array) throws JSONException {
        ArrayList<WallPaperItem> wallPaperItems = new ArrayList<>();
        for (int i = 0 ; i< array.length() ; i++) {
            WallPaperItem item = new WallPaperItem();
            item.valueOf(array.getJSONObject(i));
            wallPaperItems.add(item);
        }
        return wallPaperItems;
    }
    */
    public WallPaperItem(String file_style, String thumb_image) {
        this.file_style = file_style;

    }

    public String getFile_style() {
        return file_style;
    }

    public void setFile_style(String file_style) {
        this.file_style = file_style;
    }

}
