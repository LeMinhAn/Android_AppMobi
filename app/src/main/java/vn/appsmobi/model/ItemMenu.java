package vn.appsmobi.model;

/**
 * Created by tobrother272 on 12/24/2015.
 */
public class ItemMenu {
    private int ma_menu;
    private String name_menu;
    private int image_menu;

    public ItemMenu(int ma_menu, String name_menu, int image_menu) {
        this.ma_menu = ma_menu;
        this.name_menu = name_menu;
        this.image_menu = image_menu;
    }

    public int getMa_menu() {
        return ma_menu;
    }

    public void setMa_menu(int ma_menu) {
        this.ma_menu = ma_menu;
    }

    public String getName_menu() {
        return name_menu;
    }

    public void setName_menu(String name_menu) {
        this.name_menu = name_menu;
    }

    public int getImage_menu() {
        return image_menu;
    }

    public void setImage_menu(int image_menu) {
        this.image_menu = image_menu;
    }
}
