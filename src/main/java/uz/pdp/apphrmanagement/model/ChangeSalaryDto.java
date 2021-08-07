package uz.pdp.apphrmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSalaryDto {
    @NotNull
    private String email;

    @NotNull
    private Double salary;
}
