package uz.pdp.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.model.ApiResponse;
import uz.pdp.apphrmanagement.model.TourniquetCardDto;
import uz.pdp.apphrmanagement.model.TourniquetHistoryDto;
import uz.pdp.apphrmanagement.service.TourniquetService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/tourniquet")
public class TourniquetController {
    @Autowired
    TourniquetService tourniquetService;

    @PreAuthorize("hasAnyRole('DIRECTOR','HR_MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody TourniquetCardDto dto) {
        ApiResponse apiResponse = tourniquetService.create(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 201 : 409).body(apiResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{email}")
    public ResponseEntity<?> edit(@PathVariable String email, @Valid @RequestBody TourniquetCardDto dto){
        ApiResponse apiResponse = tourniquetService.edit(dto,email);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @PatchMapping("/update")
    public ResponseEntity<?> activate(@Valid @RequestBody TourniquetHistoryDto dto) {
        ApiResponse apiResponse = tourniquetService.activate(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/enter")
    public ResponseEntity<?> enter(@Valid @RequestBody TourniquetHistoryDto dto){
        ApiResponse apiResponse = tourniquetService.enter(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    @PostMapping("/exit")
    public ResponseEntity<?> exit(@Valid @RequestBody TourniquetHistoryDto dto){
        ApiResponse apiResponse = tourniquetService.exit(dto);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

    @PreAuthorize("hasRole('DIRECTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        ApiResponse apiResponse = tourniquetService.delete(id);
        return ResponseEntity.status(apiResponse.isStatus() ? 200 : 401).body(apiResponse);
    }

}

