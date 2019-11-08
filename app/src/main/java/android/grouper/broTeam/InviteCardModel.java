package android.grouper.broTeam;

import android.view.View;

public class InviteCardModel {
    private View.OnClickListener joinOnClickListener, deleteOnClickListener;
    private String groupTitle, groupId;

    public String getGroupTitle() {
        return groupTitle;
    }

    public String getGroupId() { return groupId; }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public void setGroupId(String groupId) { this.groupId = groupId; }
}
