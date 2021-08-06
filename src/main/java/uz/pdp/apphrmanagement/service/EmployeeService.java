package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Employee;
import uz.pdp.apphrmanagement.model.ApiResponse;
import uz.pdp.apphrmanagement.model.EmployeeLoginDto;
import uz.pdp.apphrmanagement.repository.EmployeeRepository;
import uz.pdp.apphrmanagement.repository.RoleRepository;
import uz.pdp.apphrmanagement.security.JwtProvider;

import java.util.Optional;


@Service
public class EmployeeService implements UserDetailsService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse login(EmployeeLoginDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
            Employee employee = (Employee) authentication.getPrincipal();

            String token = jwtProvider.generateToken(employee.getEmail(), employee.getRole());

            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("Login or password is incorrect", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return new User(employee.getEmail(), employee.getPassword(), employee.getAuthorities());
        }
        throw new UsernameNotFoundException("Not found");
//        return employeeRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}
