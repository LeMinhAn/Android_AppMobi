package vn.appsmobi.model;

import android.graphics.drawable.Drawable;

/**
 * Created by tobrother on 06/01/2016.
 */
public class ItemAppManage {
    private int maApp;
    private String nameApp;
    private String authorApp;
    private int statusApp;
    private String imageApp;
    private float sizeApp;
    private Drawable imageAppDrawable;

    public ItemAppManage(int maApp, String nameApp, String authorApp, int statusApp, String imageApp, float sizeApp) {
        this.maApp = maApp;
        this.nameApp = nameApp;
        this.authorApp = authorApp;
        this.statusApp = statusApp;
        this.imageApp = imageApp;
        this.sizeApp = sizeApp;
    }

    public ItemAppManage(int maApp, String nameApp, String authorApp, int statusApp, Drawable imageAppDrawable, float sizeApp) {
        this.maApp = maApp;
        this.nameApp = nameApp;
        this.authorApp = authorApp;
        this.statusApp = statusApp;
        this.imageAppDrawable = imageAppDrawable;
        this.sizeApp = sizeApp;
    }

    public int getMaApp() {
        return maApp;
    }

    public void setMaApp(int maApp) {
        this.maApp = maApp;
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public int getStatusApp() {
        return statusApp;
    }

    public void setStatusApp(int statusApp) {
        this.statusApp = statusApp;
    }

    public String getImageApp() {
        return imageApp;
    }

    public void setImageApp(String imageApp) {
        this.imageApp = imageApp;
    }

    public float getSizeApp() {
        return sizeApp;
    }

    public void setSizeApp(float sizeApp) {
        this.sizeApp = sizeApp;
    }

    public String getAuthorApp() {
        return authorApp;
    }

    public void setAuthorApp(String authorApp) {
        this.authorApp = authorApp;
    }

    public Drawable getImageAppDrawable() {
        return imageAppDrawable;
    }

    public void setImageAppDrawable(Drawable imageAppDrawable) {
        this.imageAppDrawable = imageAppDrawable;
    }
}
