package android.grouper.broTeam;

import android.view.View;

public class TaskCardModel {
    private View.OnClickListener onClickListener;
    private String title, description, groupId, taskId;
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

    public String getGroupId(){ return groupId; }

    public String getTaskId() { return taskId; }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setGroupId(String gid){ this.groupId = gid; }

    public void setTaskId(String tid) { this.taskId = tid; }
}
