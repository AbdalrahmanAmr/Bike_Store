package devs.astraweb.motoaccessoriesapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final List<String> ALLOWED_CONTENT_TYPES =
            List.of("image/jpeg", "image/png", "image/webp");

    private final Path uploadDir;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDirPath) {
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload directory: " + this.uploadDir, e);
        }
    }

    // Returns the relative URL path (e.g. "/uploads/products/xyz.jpg") to store on the Product entity
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Only JPEG, PNG, and WEBP images are allowed");
        }

        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
        String extension = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.'))
                : "";
        // Random filename avoids collisions and prevents path traversal via a crafted original filename
        String filename = UUID.randomUUID() + extension;

        try {
            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store uploaded file", e);
        }

        return "/uploads/products/" + filename;
    }

    // Stores every non-empty file in the list and returns their relative URLs, in order.
    // Skips null/empty entries instead of throwing, so one bad slot doesn't fail the whole batch.
    public List<String> storeAll(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        if (files == null) {
            return urls;
        }
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                urls.add(store(file));
            }
        }
        return urls;
    }

    // Deletes the old image file when a product's image is replaced - best-effort, does not throw on failure
    public void delete(String relativeUrl) {
        if (relativeUrl == null || relativeUrl.isBlank()) {
            return;
        }
        try {
            String filename = relativeUrl.substring(relativeUrl.lastIndexOf('/') + 1);
            Files.deleteIfExists(uploadDir.resolve(filename));
        } catch (IOException ignored) {
            // Non-critical - an orphaned file on disk is not worth failing the request over
        }
    }

    // Deletes every file in the list - best-effort, same semantics as delete()
    public void deleteAll(List<String> relativeUrls) {
        if (relativeUrls == null) {
            return;
        }
        for (String url : relativeUrls) {
            delete(url);
        }
    }
}