package com.srm325.budgetshop.model_classes;

public class Categories {
    private String categoryName;
    private String categoryBudget;
    private String categorySpent;
    private String categoryRemaining;
    private String categoryAmount;
    private String categoryPercentage;
    private boolean hasContent;
    private boolean isExpense;

    public Categories(){}

    public Categories(String categoryName, String categoryBudget, boolean hasContent, boolean isExpense) {
        this.categoryName = categoryName;
        this.categoryBudget = categoryBudget;
        this.hasContent = hasContent;
        this.isExpense = isExpense;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryBudget() {
        return categoryBudget;
    }

    public void setCategoryBudget(String categoryBudget) {
        this.categoryBudget = categoryBudget;
    }

    public void setHasContent(boolean hasContent) {
        this.hasContent = hasContent;
    }

    public boolean isHasContent() {
        return hasContent;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }

    public String getCategorySpent() {
        return categorySpent;
    }

    public void setCategorySpent(String categorySpent) {
        this.categorySpent = categorySpent;
    }

    public String getCategoryRemaining() {
        return categoryRemaining;
    }

    public void setCategoryRemaining(String categoryRemaining) {
        this.categoryRemaining = categoryRemaining;
    }

    public String getCategoryAmount() {
        return categoryAmount;
    }

    public void setCategoryAmount(String categoryAmount) {
        this.categoryAmount = categoryAmount;
    }

    public String getCategoryPercentage() {
        return categoryPercentage;
    }

    public void setCategoryPercentage(String categoryPercentage) {
        this.categoryPercentage = categoryPercentage;
    }
}
