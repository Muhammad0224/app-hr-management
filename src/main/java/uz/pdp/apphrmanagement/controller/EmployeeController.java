package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.model.ApiResponse;
import uz.pdp.apphrmanagement.model.ChangeSalaryDto;
import uz.pdp.apphrmanagement.model.EmployeeDto;
import uz.pdp.apphrmanagement.model.EmployeeLoginDto;
import uz.pdp.apphrmanagement.service.EmployeeService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody EmployeeLoginDto dto) {
        ApiResponse apiResponse = employeeService.login(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit(@RequestBody EmployeeDto email, @PathVariable String id) {
        ApiResponse apiResponse = employeeService.edit(email, id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @PutMapping("/edit/{email}")
    public ResponseEntity<?> editSalary(@RequestBody ChangeSalaryDto dto) {
        ApiResponse apiResponse = employeeService.editSalary(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR','HR_MANAGER')")
    @GetMapping
    public ResponseEntity<?> get() {
        ApiResponse apiResponse = employeeService.get();
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    // from and to should be "yyyy-mm-dd" format
    @PreAuthorize("hasAnyRole('DIRECTOR','HR_MANAGER')")
    @GetMapping("/{email}")
    public ResponseEntity<?> info(@PathVariable String email, @RequestParam String from, @RequestParam String to) {
        ApiResponse apiResponse = employeeService.info(email, from, to);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR','HR_MANAGER')")
    @GetMapping("/salary/{email}")
    public ResponseEntity<?> infoSalary(@PathVariable String email, @RequestParam String from, @RequestParam String to) {
        ApiResponse apiResponse = employeeService.infoSalary(email, from, to);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR','HR_MANAGER')")
    @GetMapping("/task/{email}")
    public ResponseEntity<?> infoTask(@PathVariable String email, @RequestParam String from, @RequestParam String to) {
        ApiResponse apiResponse = employeeService.infoTask(email, from, to);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }
}
