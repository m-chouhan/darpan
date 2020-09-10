 package darpan;

import java.util.List;

/**
 *
 * @author DarpanTeam
 */
public class PMDarpanModel {

    private int Instance_Code;
    private int Sec_Code;
    private int Ministry_Code;
    private int Dept_Code;
    private int Project_Code;
    private int Frequency_Id;
    private int atmpt;
    private List<KPIDetails> ListKpidata;

    public PMDarpanModel() {
    }

    public PMDarpanModel(int instanceCode, int secCode, int ministryCode, int deptCode, int projectCode, int frequencyId, int atmpt, List<KPIDetails> kpiDetails) {
        this.Instance_Code = instanceCode;
        this.Sec_Code = secCode;
        this.Ministry_Code = ministryCode;
        this.Dept_Code = deptCode;
        this.Project_Code = projectCode;
        this.Frequency_Id = frequencyId;
        this.atmpt = atmpt;
        this.ListKpidata = kpiDetails;
    }

    public int getInstanceCode() {
        return Instance_Code;
    }

    public void setInstanceCode(int instanceCode) {
        this.Instance_Code = instanceCode;
    }

    public int getSecCode() {
        return Sec_Code;
    }

    public void setSecCode(int secCode) {
        this.Sec_Code = secCode;
    }

    public int getMinistryCode() {
        return Ministry_Code;
    }

    public void setMinistryCode(int ministryCode) {
        this.Ministry_Code = ministryCode;
    }

    public int getDeptCode() {
        return Dept_Code;
    }

    public void setDeptCode(int deptCode) {
        this.Dept_Code = deptCode;
    }

    public int getProjectCode() {
        return Project_Code;
    }

    public void setProjectCode(int projectCode) {
        this.Project_Code = projectCode;
    }

    public int getFrequencyId() {
        return Frequency_Id;
    }

    public void setFrequencyId(int frequencyId) {
        this.Frequency_Id = frequencyId;
    }

    public int getAtmpt() {
        return atmpt;
    }

    public void setAtmpt(int atmpt) {
        this.atmpt = atmpt;
    }

    public List getKpiDetails() {
        return ListKpidata;
    }

    public void setKpiDetails(List kpiDetails) {
        this.ListKpidata = kpiDetails;
    }

}
