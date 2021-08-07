package uz.pdp.apphrmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer deadlineDay;

    @NotNull
    private String email;
}
