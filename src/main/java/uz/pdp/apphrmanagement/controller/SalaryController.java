package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.model.ApiResponse;
import uz.pdp.apphrmanagement.model.SalaryDto;
import uz.pdp.apphrmanagement.service.SalaryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    @Autowired
    SalaryService salaryService;

    @PreAuthorize("hasAnyRole('DIRECTOR', 'HR_MANAGER')")
    @PostMapping("/pay")
    public ResponseEntity<?> pay(@Valid @RequestBody SalaryDto dto){
        ApiResponse apiResponse = salaryService.pay(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam String email, @RequestParam String  emailCode){
        ApiResponse apiResponse = salaryService.confirm(email, emailCode);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/reject")
    public ResponseEntity<?> reject(@RequestParam String email, @RequestParam String  emailCode){
        ApiResponse apiResponse = salaryService.reject(email, emailCode);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }
}
