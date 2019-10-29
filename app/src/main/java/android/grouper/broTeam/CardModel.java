package android.grouper.broTeam;

import android.view.View;

public class CardModel {

    private View.OnClickListener onClickListener;
    private String title, description, groupId;
    private int img;

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public int getImg(){
        return img;
    }

    public String getGroupID(){ return groupId; }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setGroupId(String gid){this.groupId = gid; }
}

