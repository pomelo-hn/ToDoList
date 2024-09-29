import main.java.toDoList.service.TaskService;
import main.java.toDoList.service.XMLTaskRepository;

import java.io.File;
import java.util.Scanner;

public class ToDoListApplication {

    private TaskService taskService;

    public ToDoListApplication(TaskService taskService) {
        this.taskService = taskService;
    }

    public static void main(String[] args) {
        ToDoListApplication app = new ToDoListApplication(
                new TaskService(new XMLTaskRepository(new File("src/main/resources/toDoList.xml"))));
        app.run();
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.print("Введите команду (введите 'help' для получения списка команд): ");
            String command = scanner.nextLine().trim();

            // Проверка на выход из цикла
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Выход из программы...");
                running = false;
            }

            if (command.startsWith("help")) {
                System.out.printf("%-15s %s%n", "new", "Добавить новую задачу");
                System.out.printf("  Аргументы: %s%n", "заголовок, описание, важность и срок задачи через запятую\n");

                System.out.printf("%-15s %s%n", "complete", "Пометить задачу как выполненную");
                System.out.printf("  Аргументы: %s%n", "id\n");

                System.out.printf("%-15s %s%n", "edit", "Измененить задачу");
                System.out.printf("  Аргументы: %s%n", "id\n");

                System.out.printf("%-15s %s%n", "list", "Вывести задачи");
                System.out.printf("  Аргументы: %s%n", "\n -s new,\n -s done,\n -s in_progress\n");

                System.out.printf("%-15s %s%n", "remove", "Удалить задачу");
                System.out.printf("  Аргументы: %s%n", "id\n");
            } else if (command.startsWith("new")) {

                taskService.addTask(command);

            } else if (command.startsWith("edit")) {

                taskService.editTask(command, scanner);

            } else if (command.startsWith("remove")) {

                taskService.removeTask(command);

            } else if (command.startsWith("list")) {

                taskService.listTask(command);

            } else if (command.startsWith("complete")) {

                taskService.completeTask(command);

            } else {
                System.out.println("Неизвестная команда. Пожалуйста, попробуйте снова.");
            }
        }
        scanner.close();
    }

}