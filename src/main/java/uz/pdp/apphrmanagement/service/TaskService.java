package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Employee;
import uz.pdp.apphrmanagement.entity.Task;
import uz.pdp.apphrmanagement.model.ApiResponse;
import uz.pdp.apphrmanagement.model.TaskDto;
import uz.pdp.apphrmanagement.repository.EmployeeRepository;
import uz.pdp.apphrmanagement.repository.TaskRepository;

import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    JavaMailSender javaMailSender;

    public ApiResponse createForManager(TaskDto dto) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmailAndEnabledTrue(dto.getEmail());
        if (!optionalEmployee.isPresent()) {
            return new ApiResponse("Employee not found", false);
        }
        Employee employee = optionalEmployee.get();
        if (employeeRepository.isManager(employee.getEmail())) {

            Task task = new Task();
            task.setDeadline(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * dto.getDeadlineDay()));
            task.setDescription(dto.getDescription());
            task.setEmployee(employee);
            task.setName(dto.getName());
            task.setTaskCode(UUID.randomUUID().toString());

            Task savedTask = taskRepository.save(task);
            sendTask(employee, savedTask);
            return new ApiResponse("Task created!", true);
        }
        return new ApiResponse(employee.getEmail() + " is not manager", false);
    }

    public ApiResponse createForWorker(TaskDto dto) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmailAndEnabledTrue(dto.getEmail());
        if (!optionalEmployee.isPresent()) {
            return new ApiResponse("Employee not found", false);
        }
        Employee employee = optionalEmployee.get();
        if (employeeRepository.isWorker(employee.getEmail())) {

            Task task = new Task();
            task.setDeadline(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * dto.getDeadlineDay()));
            task.setDescription(dto.getDescription());
            task.setEmployee(employee);
            task.setName(dto.getName());
            task.setTaskCode(UUID.randomUUID().toString());

            Task savedTask = taskRepository.save(task);
            sendTask(employee, savedTask);
            return new ApiResponse("Task created!", true);
        }
        return new ApiResponse(employee.getEmail() + " is not worker", false);
    }

    public void sendTask(Employee employee, Task savedTask) {
        String link = "http://localhost:8080/api/task/confirm?email=" + employee.getEmail() + "&taskCode=" + savedTask.getTaskCode();
        String body = "<form action=" + link + " method=\"post\">\n" +
                "<p>" + savedTask.getDescription() + "</p>" +
                "<p>Task code=" + savedTask.getTaskCode() + "</p>" +
                "<button style=\"padding: 5px 10px; background-color: #24d024; margin-top: 5px; color: white \">Submit</button>\n" +
                "</form>";
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(savedTask.getCreatedBy());
            Employee from = optionalEmployee.get();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setSubject(savedTask.getName());
            helper.setFrom(from.getEmail());
            helper.setTo(employee.getEmail());
            helper.setText(body, true);
            javaMailSender.send(message);
            employee.setEmailCode(savedTask.getTaskCode());
            employeeRepository.save(employee);
        } catch (Exception ignored) {
        }
    }

    public ApiResponse confirm(String email, String taskCode) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmailAndEmailCode(email, taskCode);
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Email or code isn't correct", false);
        Optional<Task> optionalTask = taskRepository.findByTaskCode(taskCode);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task code isn't correct", false);
        Task task = optionalTask.get();
        Employee employee = optionalEmployee.get();
        task.setStatus(2);
        employee.setEmailCode(null);
        taskRepository.save(task);
        employeeRepository.save(employee);
        return new ApiResponse("Task confirmed", true);
    }

    public ApiResponse completed(String taskCode) {
        Optional<Task> optionalTask = taskRepository.findByTaskCode(taskCode);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task not found", false);
        Task task = optionalTask.get();
        if (task.getDeadline().before(new Date(System.currentTimeMillis())))
            return new ApiResponse("Submission deadline", false);
        if (task.getStatus() == 1)
            return new ApiResponse("Task is not confirmed", false);
        if (task.getStatus() == 3)
            return new ApiResponse("Task has already completed", false);

        task.setStatus(3);
        task.setCompletedAt(Timestamp.valueOf(LocalDateTime.now()));
        Task savedTask = taskRepository.save(task);

        sendForComplete(savedTask);
        return new ApiResponse("Complete confirmed!", true);
    }

    private void sendForComplete(Task task) {
        String body = "<p>Task: " + task.getName() + "</p>" +
                "<p>Description: " + task.getDescription() + "</p>" +
                "<p>Completed: " + task.getCompletedAt() + "</p>";
        try {
            Employee from = employeeRepository.getById(task.getCreatedBy());
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setSubject("Task completed");
            helper.setFrom(from.getEmail());
            helper.setTo(employeeRepository.getById(task.getCreatedBy()).getEmail());
            helper.setText(body, true);
            javaMailSender.send(message);
        } catch (Exception ignored) {
        }


    }
}
