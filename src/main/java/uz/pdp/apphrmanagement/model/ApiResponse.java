package uz.pdp.apphrmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private boolean status;
    private Object object;

    public ApiResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }
}
