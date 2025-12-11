package com.scm.validators;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2 MB

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        // ✅ Allow null or empty file (example: update without changing image)
        if (file == null || file.isEmpty()) {
            return true;
        }

        // ✅ Check file size limit
        if (file.getSize() > MAX_FILE_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "File size must be less than " + (MAX_FILE_SIZE / (1024 * 1024)) + " MB"
            ).addConstraintViolation();
            return false;
        }

        // ✅ Optionally, check if the file is an image
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Only image files are allowed")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}
