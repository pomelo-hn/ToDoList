package test.java.toDoList.service;

import main.java.toDoList.model.StatusOfTask;
import main.java.toDoList.model.Task;
import main.java.toDoList.service.TaskService;
import main.java.toDoList.service.XMLTaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    private TaskService taskService;
    private XMLTaskRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new XMLTaskRepository(new File("src/main/resources/toDoList.xml")); // Предполагая, что это простой репозиторий
        taskService = new TaskService(repository);
    }

    @AfterEach
    public void tearDown() {
        Document document = repository.loadDocument();
        Element root = document.getDocumentElement();
        NodeList taskList = document.getElementsByTagName("Task");

        for (int i = 0; i < taskList.getLength(); i++) {
            Element task = (Element) taskList.item(i);
            root.removeChild(task);
        }

        repository.saveDocument(document);
    }

    @Test
    public void testAddTask_ValidInput() {

        String taskName = "Задача1";
        String description = "Описание задачи";
        int priority = 5;
        LocalDate deadline = LocalDate.parse("2024-10-20");

        taskService.addTask("new " + taskName + ", " + description + ", " + priority + ", " + deadline);

        List<Task> tasks = repository.getTasks();
        assertEquals(1, tasks.size());
    }

    @Test
    public void testCompleteTask_ValidTask() {

        String taskName = "Задача1";
        String description = "Описание задачи";
        int priority = 5;
        LocalDate deadline = LocalDate.parse("2024-10-20");
        taskService.addTask("new " + taskName + ", " + description + ", " + priority + ", " + deadline);

        taskService.completeTask("complete 1");

        Task completedTask = repository.findTaskById(1);
        assertEquals(StatusOfTask.DONE, completedTask.getStatusOfTask());
    }

    @Test
    public void testRemoveTask_ValidTask() {

        String taskName = "Задача1";
        String description = "Описание задачи";
        int priority = 5;
        LocalDate deadline = LocalDate.parse("2024-10-20");
        taskService.addTask("new " + taskName + ", " + description + ", " + priority + ", " + deadline);

        taskService.removeTask("remove 1");

        List<Task> tasks = repository.getTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testGetTaskByName_NonExistingTask() {

        Task foundTask = repository.findTaskById(1);

        assertNull(foundTask);
    }
}