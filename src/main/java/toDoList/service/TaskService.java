package main.java.toDoList.service;

import main.java.toDoList.model.StatusOfTask;
import main.java.toDoList.model.Task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


/**
 * Класс, содержащий в себе бизнес-логику для управления задачами.
 * Обрабатывает входящие команды и исключения.
 * По итогам обработки передает необходимые параметры в методы класса <code>XMLTaskRepository</code>.
 */
public class TaskService {
    private final XMLTaskRepository taskRepository;

    public TaskService(XMLTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Разделяет полученную команду на аргументы <code>caption</code>, <code>description</code>, <code>priority</code>,
     * <code>deadline</code>.
     * проверяет на количество аргументов и их соответствие к ограничениям.
     * Создает новый объект класса <code>Task</code> и передает его в метод <code>createTask(Task task)</code> класса
     * <code>XMLTaskRepository</code> который создает соответствующую запись в XML
     *
     * @param command команда из консоли
     * @throws StringIndexOutOfBoundsException если аргументы команд в некорректном формате
     * @throws StringLengthExceededException если <code>caption</code> более 50 символов
     * @throws NumberFormatException если <code>priority</code> более 10 или менее 0
     * @throws DateTimeParseException если <code>deadline</code> передано в некорректном формате
     */
    public void addTask(String command) {
        try {
            String[] parts = command.substring(4).split(", ");

            if (parts.length != 4) {
                throw new StringIndexOutOfBoundsException();
            }

            if (parts[0].length() > 50){
                throw new StringLengthExceededException();
            } else if ((Integer.parseInt(parts[2]) > 10) || (Integer.parseInt(parts[2]) < 0)){
                throw new NumberFormatException();
            }

            Task newTask = new Task(
                    parts[0],
                    parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDate.parse(parts[3]));
            Task task = taskRepository.createTask(newTask);
            System.out.println("Создана задача. " + task.toString());
        } catch (StringLengthExceededException e) {
            System.out.println("Ошибка: заголовок должен содержать не более 50 символов");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: приоритет должен быть целым числом в диапазоне от 0 до 10");
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: некорреткный формат ввода даты. Пример: YYYY-MM-DD");
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Ошибка: некорректный формат команды. Пример: new Выкинуть мусор, " +
                    "Выкинуть мусор из дома, 10, 2024-10-20");
        }

    }

    /**
     * Разделяет полученную команду и извлекает из нее значение <code>id</code>.
     * Проверяет на соответствие ограничениям.
     * В случае, если <code>Task</code> с таким <code>id</code> из XML найдено, выводит его в консоль и запрашивает новые
     * параметры для этого <code>Task</code>. Ввод также проверяется на соответствие ограничениям.
     * Передает полученные значения ввода в
     * <code>editTask(id, newCaption, newDescription, newPriority, newDeadline, newStatus)</code>
     * класса <code>XMLTaskRepository</code>.
     * <p> Если возврат <code>editTask</code> равен <code>false</code>, то вызывается сообщение об ошибке.
     * Иначе на консоль выводится сообщение об успешном редактировании задачи
     *
     * @param command команда из консоли
     * @param scanner объект класса <code>Scanner</code> для ввода новых параметров
     *
     * @throws StringLengthExceededException если <code>caption</code> более 50 символов
     * @throws NumberFormatException если <code>priority</code> более 10 или менее 0
     * @throws DateTimeParseException если <code>deadline</code> передано в некорректном формате
     * @throws IllegalArgumentException если <code>status</code> не содержится в enum <code>StatusOfTask</code>
     */
    public void editTask(String command, Scanner scanner) {
        String[] parts = command.split(" ");
        if (parts.length < 2) {
            System.out.println("Ошибка: некорректный формат команды. Пример: edit 1");
            return;
        }

        try{
            int id = Integer.parseInt(parts[1]);

            Task task = taskRepository.findTaskById(id);

            System.out.println("Найдена задача. " + task.toString());

            String newCaption = inputParameters("заголовок", scanner);
            if (newCaption.length() > 50){
                throw new StringLengthExceededException();
            }
            String newDescription = inputParameters("описание", scanner);
            String newPriorityInput = inputParameters("приоритет", scanner);
            int newPriority = newPriorityInput.isEmpty()
                    ? 0
                    : Integer.parseInt(newPriorityInput);
            if ((newPriority > 10) || (newPriority < 0)){
                throw new NumberFormatException();
            }
            String newDeadlineInput = inputParameters("срок", scanner);
            LocalDate newDeadline = newDeadlineInput.isEmpty()
                    ? null
                    : LocalDate.parse(newDeadlineInput);
            String newStatusInput = inputParameters("статус", scanner);
            StatusOfTask newStatus = newStatusInput.isEmpty()
                    ? task.getStatusOfTask()
                    : StatusOfTask.valueOf(newStatusInput.toUpperCase());

            if(!taskRepository.editTask(id, newCaption, newDescription, newPriority, newDeadline, newStatus)){
                System.out.println("Задача не найдена");
                return;
            }

            System.out.println("Задача " + id + " успешно отредактирована!");
        } catch (StringLengthExceededException e) {
            System.out.println("Ошибка: заголовок должен содержать не более 50 символов");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ввод должен быть целым числом. Приоритет в диапазоне от 0 до 10");
        } catch (DateTimeParseException e) {
            System.out.println("Некорректный формат ввода даты! Пример: YYYY-MM-DD. Попробуйте еще раз");
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный формат ввода статуса! Возможные статусы: new, in_progress, done. Попробуйте еще раз");
        }
    }

    /**
     * Разделяет полученную команду на аргументы. Аргументами могут быть <code>-s new</code>, <code>-s done</code>,
     * <code>-s in_progress</code>. В случае если в команде содержится только <code>list</code>, то выводится список
     * из всех задач, отсортированный по <code>id</code>. Аргументы <code>-s new</code>, <code>-s done</code>,
     * <code>-s in_progress</code> выводят список из новых/выполненных/в процессе задач соответственно.
     * <p>Вывод всех задач происходит благодаря <code>ArrayList</code> из <code>Task</code>, которые были получены из
     * <code>getTasks()</code> и выводятся циклом <code>for</code>. <code>ArrayList</code> из задач по статусу получаем
     * через <code>viewTasksByStatus()</code>
     * <p>В случае если команда введена неверно, вызывается соответствующее сообщение об ошибке.
     *
     * @param command команда из консоли
     */
    public void listTask(String command){
        String[] parts = command.split(" ");
        List<Task> tasks = taskRepository.getTasks();

        if (parts.length == 1 && parts[0].equals("list")) {
            for (Task task : tasks) {
                System.out.println(task);
            }
        } else if (parts.length == 3 && parts[0].equals("list") && parts[1].equals("-s")) {
            List<Task> taskByStatus = taskRepository.viewTasksByStatus(tasks, StatusOfTask.valueOf(parts[2].toUpperCase()));
            for (Task task : taskByStatus) {
                System.out.println(task);
            }
        } else {
            System.out.println("Ошибка: некорректный формат команды. Пример: list -s new");
        }
    }

    /**
     * Разделяет полученную команду и извлекает из нее значение <code>id</code>.
     * В случае, если <code>Task</code> с таким <code>id</code> из XML найдено, вызывается функция
     * <code>deleteTask()</code>, которая удаляет соответствующую запись из XML.
     * <p>В случае если команда введена неверно или задача не найдена, вызывается соответствующее сообщение об ошибке.
     *
     * @param command команда из консоли
     * @throws NumberFormatException если <code>id</code> не удалось переконвертировать в int, т.е. если
     * на вход было получено не целое число
     */
    public void removeTask(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 2) {
            System.out.println("Ошибка: некорректный формат команды. Пример: delete 1");
            return;
        }

        try {


            int id = Integer.parseInt(parts[1]);

            if (!taskRepository.deleteTask(id)) {
                System.out.println("Задача не найдена");
                return;
            }

            System.out.println("Задача " + id + " успешно удалена!");
        } catch (NumberFormatException e){
            System.out.println("Аргумент id должен быть целым числом");
        }
    }

    /**
     * Разделяет полученную команду и извлекает из нее значение <code>id</code>.
     * В случае, если <code>Task</code> с таким <code>id</code> из XML найдено, вызывается функция
     * <code>markAsCompleted()</code>, которая корректирует статус задачи на "done", добавляет элеменет
     * "Complete" в XML.
     * <p>В случае если команда введена неверно, задача не найдена или уже выполнена,
     * вызывается соответствующее сообщение об ошибке.
     *
     * @param command команда из консоли
     * @throws NumberFormatException если <code>id</code> не удалось переконвертировать в int, т.е. если
     * на вход было получено не целое число
     */
    public void completeTask(String command){
        String[] parts = command.split(" ");
        if (parts.length < 2) {
            System.out.println("Ошибка: некорректный формат команды. Пример: delete 1");
            return;
        }

        try {


            int id = Integer.parseInt(parts[1]);

            if (!taskRepository.markAsCompleted(id)) {
                System.out.println("Задача не найдена или уже выполнена");
                return;
            }

            System.out.println("Задача " + id + " успешно отмечена как выполненная!");
        } catch (NumberFormatException e){
            System.out.println("Аргумент id должен быть целым числом");
        }

    }

    /**
     * Вспомогательный метод для <code>editTask()</code>.
     * Вывод сообщение о значении, которое мы хотим изменить в задаче и получает на ввод строку с измененными
     * параметрами для задачи.
     *
     * @param type тип элемента, который мы хотим изменить
     * @param scanner сканнер для ввода новых параметров от пользователя
     * @return новый параметр, необходимый для изменения в <code>editTask()</code>
     */
    private String inputParameters(String type, Scanner scanner){
        System.out.println("Введите новое значение " + type.toLowerCase() + " или нажмите Enter, чтобы оставить без изменений:");
        return scanner.nextLine();
    }

}
