package com.scm.forms;

  
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
    
    @NotBlank(message = "Username is required")
    @Size(min=3,message="min 3 character is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message="Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "About is required")
    private String about;

    @NotBlank(message = "Phone number is required")
    @Size(min=8,max=12,message="Invalid phone number")
    private String phoneNumber;
}
