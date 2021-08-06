package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.apphrmanagement.model.ApiResponse;
import uz.pdp.apphrmanagement.model.EmployeeLoginDto;
import uz.pdp.apphrmanagement.service.EmployeeService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody EmployeeLoginDto dto){
        ApiResponse apiResponse = employeeService.login(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }
}
