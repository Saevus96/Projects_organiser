package pl.kpchl.registrationproject.models;

public class GroupClass {
    private String groupName;
    private String groupAdmin;
    private String groupSpeciality;

    public GroupClass() {
    }

    public GroupClass(String groupName, String groupAdmin, String groupSpeciality) {
        this.groupName = groupName;
        this.groupAdmin = groupAdmin;
        this.groupSpeciality = groupSpeciality;
    }

    public String getGroupSpeciality() {
        return groupSpeciality;
    }

    public void setGroupSpeciality(String groupSpeciality) {
        this.groupSpeciality = groupSpeciality;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }
}
