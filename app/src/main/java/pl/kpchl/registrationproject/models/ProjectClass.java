package pl.kpchl.registrationproject.models;

//project model
public class ProjectClass {
    private String projectName;
    private String projectCategory;
    private String projectDescription;
    private String projectAdmin;
    private String projectOrganisation;
    private String projectCustomers;

    public ProjectClass() {}

    public ProjectClass(String projectName, String projectCategory, String projectDescription, String projectAdmin, String projectOrganisation, String projectCustomers) {
        this.projectName = projectName;
        this.projectCategory = projectCategory;
        this.projectDescription = projectDescription;
        this.projectAdmin = projectAdmin;
        this.projectOrganisation = projectOrganisation;
        this.projectCustomers = projectCustomers;
    }

    public String getProjectOrganisation() {
        return projectOrganisation;
    }

    public void setProjectOrganisation(String projectOrganisation) {
        this.projectOrganisation = projectOrganisation;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectAdmin() {
        return projectAdmin;
    }

    public void setProjectAdmin(String projectAdmin) {
        this.projectAdmin = projectAdmin;
    }

    public String getProjectCustomers() {
        return projectCustomers;
    }

    public void setProjectCustomers(String projectCustomers) {
        this.projectCustomers = projectCustomers;
    }
}
