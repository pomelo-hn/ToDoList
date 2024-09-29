package main.java.toDoList.model;

import java.time.LocalDate;

public class Task {
    private int id;
    private String caption;
    private String description;
    private int priority;
    private LocalDate deadline;
    private StatusOfTask statusOfTask;
    private LocalDate completionDate;

    public Task(String caption, String description, int priority, LocalDate deadline) {
        this.caption = caption;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
    }

    public Task(int id, String caption, String description, int priority, LocalDate deadline, StatusOfTask statusOfTask) {
        this.id = id;
        this.caption = caption;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.statusOfTask = statusOfTask;
    }

    @Override
    public String toString() {
        if (completionDate != null) {
            return "\nЗадача ID: " + id +
                    ". \nЗаголовок: " + caption +
                    ". \nОписание: " + description +
                    ". \nПриоритет: " + priority +
                    ". \nСрок: до " + deadline +
                    ". \nСтатус: " + statusOfTask +
                    ". \nДата выполнения: " + completionDate;
        } else {
            return "\nЗадача ID: " + id +
                    ". \nЗаголовок: " + caption +
                    ". \nОписание: " + description +
                    ". \nПриоритет: " + priority +
                    ". \nСрок: до " + deadline +
                    ". \nСтатус: " + statusOfTask;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public StatusOfTask getStatusOfTask() {
        return statusOfTask;
    }

    public void setStatusOfTask(StatusOfTask statusOfTask) {
        this.statusOfTask = statusOfTask;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
}
