package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ContactForm {
@NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = " email is required")
    @Email(message = "Invalid email format[example@gmail.com]")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    @NotBlank(message = "Address is required")
    private String address;
    private String description;
     private boolean favorite;
    private String websiteLink;
    private String linkedInLink;

@ValidFile(message = "Please upload a valid image file")
private MultipartFile contactImage;

     private String picture;
       // --- Getters and Setters ---
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

  // getter & setter
public MultipartFile getContactImage() { return contactImage; }
public void setContactImage(MultipartFile contactImage) { this.contactImage = contactImage; }
}
