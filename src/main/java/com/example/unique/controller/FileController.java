

package com.example.unique.controller;

import com.example.unique.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("file1") MultipartFile file1,
                                              @RequestParam("file2") MultipartFile file2) {
        try {
            // Ensure the upload directory exists
            Files.createDirectories(Path.of(UPLOAD_DIR));

            // Save files temporarily
            Path path1 = Path.of(UPLOAD_DIR + file1.getOriginalFilename());
            Path path2 = Path.of(UPLOAD_DIR + file2.getOriginalFilename());
            Files.write(path1, file1.getBytes(), StandardOpenOption.CREATE);
            Files.write(path2, file2.getBytes(), StandardOpenOption.CREATE);

            // Output file path
            Path outputPath = Path.of(UPLOAD_DIR + "unique_numbers.txt");

            // Process files
            fileService.findUniqueNumbers(path1, path2, outputPath);

            return ResponseEntity.ok("File processed successfully. Download from /api/files/download");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing files: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile() throws IOException {
        Path filePath = Path.of(UPLOAD_DIR + "unique_numbers.txt");
        byte[] fileContent = Files.readAllBytes(filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=unique_numbers.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileContent);
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteFiles() {
        try {
            Files.walk(Path.of(UPLOAD_DIR))
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (file.exists()) {
                            file.delete();
                        }
                    });
            return ResponseEntity.ok("All uploaded and processed files deleted successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error deleting files: " + e.getMessage());
        }
    }
}
