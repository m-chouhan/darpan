 
package darpan;

/**
 *
 * @author DarpanTeam
 */
public class KPIDetails {

    private int Group_Id;
    private String datadate;
    private String KValue;
    private String LValue;

    public KPIDetails() {
    }

    public KPIDetails(int groupId, String datadate, String KValue, String LValue) {
        this.Group_Id = groupId;
        this.datadate = datadate;
        this.KValue = KValue;
        this.LValue = LValue;
    }

    public int getGroupId() {
        return Group_Id;
    }

    public void setGroupId(int groupId) {
        this.Group_Id = groupId;
    }

    public String getDatadate() {
        return datadate;
    }

    public void setDatadate(String datadate) {
        this.datadate = datadate;
    }

    public String getKValue() {
        return KValue;
    }

    public void setKValue(String KValue) {
        this.KValue = KValue;
    }

    public String getLValue() {
        return LValue;
    }

    public void setLValue(String LValue) {
        this.LValue = LValue;
    }

}
