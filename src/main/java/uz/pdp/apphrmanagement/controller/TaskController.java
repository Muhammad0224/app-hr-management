package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.model.ApiResponse;
import uz.pdp.apphrmanagement.model.TaskDto;
import uz.pdp.apphrmanagement.service.TaskService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PreAuthorize("hasRole('DIRECTOR')")
    @PostMapping("/manager")
    public ResponseEntity<?> createForManager(@Valid @RequestBody TaskDto dto){
        ApiResponse apiResponse = taskService.createForManager(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'HR_MANAGER')")
    @PostMapping("/worker")
    public ResponseEntity<?> createForWorker(@Valid @RequestBody TaskDto dto){
        ApiResponse apiResponse = taskService.createForWorker(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam String email, @RequestParam String taskCode){
        ApiResponse apiResponse = taskService.confirm(email, taskCode);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'HR_MANAGER', 'WORKER')")
    @PostMapping("/completed/{taskCode}")
    public ResponseEntity<?> completed(@PathVariable String taskCode){
        ApiResponse apiResponse = taskService.completed(taskCode);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasRole('DIRECTOR')")
    @DeleteMapping("/{taskCode}")
    public ResponseEntity<?> delete(@PathVariable String taskCode) {
        ApiResponse apiResponse = taskService.delete(taskCode);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }
}
