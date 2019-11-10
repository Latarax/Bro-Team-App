package android.grouper.broTeam;

public class MessageModel {
    private String messageUserName;
    private String messageText;
    private String messageUserID;

    /*
    public MessageModel(String messageUserName, String messageText, String messageUserID){
        this.messageText = messageText;
        this.messageUserName = messageUserName;
        this.messageUserID = messageUserID;
    }
     */

    public String getMessageUserName() {
        return messageUserName;
    }

    public void setMessageUserName(String messageUserName) {
        this.messageUserName = messageUserName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUserID() {
        return messageUserID;
    }

    public void setMessageUserID(String messageUserID) {
        this.messageUserID = messageUserID;
    }
}
