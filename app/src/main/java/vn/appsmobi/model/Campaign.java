package vn.appsmobi.model;

/**
 * Created by tobrother on 17/02/2016.
 */

public class Campaign {
    private boolean hasCampaign;
    private boolean isShow;
    private String mCampainName;
    private String mCampaignUrl;
    private String mCampaignPromoUrl;
    private int mCampaignExpire;
    private CampaignType mType;
    private boolean requireLogin;

    public enum CampaignType {
        CAMPAIGN,
        APPDETAIL
    }

    public Campaign() {
        setHasCampaign(false);
        setRequireLogin(false);
    }

    public String getmCampainName() {
        return mCampainName;
    }

    public void setmCampainName(String mCampainName) {
        this.mCampainName = mCampainName;
    }

    public String getmCampaignUrl() {
        return mCampaignUrl;
    }

    public void setmCampaignUrl(String mCampaignUrl) {
        this.mCampaignUrl = mCampaignUrl;
    }

    public String getmCampaignPromoUrl() {
        return mCampaignPromoUrl;
    }

    public void setmCampaignPromoUrl(String mCampaitnPromoUrl) {
        this.mCampaignPromoUrl = mCampaitnPromoUrl;
    }

    public int getmCampaignExpire() {
        return mCampaignExpire;
    }

    public void setmCampaignExpire(int mCampaignExpire) {
        this.mCampaignExpire = mCampaignExpire;
    }

    public boolean hasCampaign() {
        return hasCampaign;
    }

    public void setHasCampaign(boolean hasCampaign) {
        this.hasCampaign = hasCampaign;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public CampaignType getmType() {
        return mType;
    }

    public void setmType(CampaignType mType) {
        this.mType = mType;
    }

    public boolean isRequireLogin() {
        return requireLogin;
    }

    public void setRequireLogin(boolean requireLogin) {
        this.requireLogin = requireLogin;
    }


}
