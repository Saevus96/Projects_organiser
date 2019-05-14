package pl.kpchl.registrationproject.models;

public class TaskClass {
    private String taskName;
    private String taskDescription;
    private String taskDateStart;
    private String taskDateEnd;
    private String taskGroupId;

    public TaskClass() {}

    public TaskClass(String taskName, String taskDescription, String taskDateStart, String taskDateEnd, String taskGroupId) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDateStart = taskDateStart;
        this.taskDateEnd = taskDateEnd;
        this.taskGroupId = taskGroupId;
    }

    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDateStart() {
        return taskDateStart;
    }

    public void setTaskDateStart(String taskDateStart) {
        this.taskDateStart = taskDateStart;
    }

    public String getTaskDateEnd() {
        return taskDateEnd;
    }

    public void setTaskDateEnd(String taskDateEnd) {
        this.taskDateEnd = taskDateEnd;
    }
}
