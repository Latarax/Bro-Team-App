package android.grouper.broTeam;

public class CardModel {

    private String title, description;
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

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setImg(int img) {
        this.img = img;
    }


}

