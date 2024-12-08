package com.example.comp3074_groupproject_bestulator;

public class Project {
    private long id;
    private String projectName;
    private String city;
    private String state;
    private String clientName;
    private String phone;
    private String email;
    private String startDate;
    private String endDate;
    private String category;
    private double budget;

    // Default constructor
    public Project() {}

    // Constructor without ID (for creating new projects)
    public Project(String projectName, String city, String state, String clientName,
                   String phone, String email, String startDate, String endDate,
                   String category, double budget) {
        this.projectName = projectName;
        this.city = city;
        this.state = state;
        this.clientName = clientName;
        this.phone = phone;
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.budget = budget;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }
}