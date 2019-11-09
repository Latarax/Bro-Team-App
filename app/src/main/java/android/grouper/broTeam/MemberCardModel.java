package android.grouper.broTeam;

public class MemberCardModel {
    private String title;
    private String groupId;
    private String memberId;

    public String getMemberId() { return memberId; }

    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getTitle() {
        return title;
    }

    public String getGroupId() { return groupId; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGroupId(String groupId) { this.groupId = groupId; }

}
