package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.model.ApiResponse;
import uz.pdp.apphrmanagement.model.EmployeeHireDto;
import uz.pdp.apphrmanagement.service.HireService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/hire")
public class HireController {
    @Autowired
    HireService hireService;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/director")
    public ResponseEntity<?> addDirector(@Valid @RequestBody EmployeeHireDto dto){
        ApiResponse apiResponse = hireService.addDirector(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @PostMapping("/manager")
    public ResponseEntity<?> addManager(@Valid @RequestBody EmployeeHireDto dto) {
        ApiResponse apiResponse = hireService.addManager(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'HR_MANAGER')")
    @PostMapping("/worker")
    public ResponseEntity<?> addWorker(@Valid @RequestBody EmployeeHireDto dto) {
        ApiResponse apiResponse = hireService.addWorker(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPassword(@RequestParam String emailCode, @RequestParam String email, HttpServletRequest request) {
        ApiResponse apiResponse = hireService.verify(emailCode, email, request);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

}
