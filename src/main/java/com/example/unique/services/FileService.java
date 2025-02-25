

// package com.example.unique.services;

// import com.example.unique.repository.FileRecordRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.util.HashSet;
// import java.util.Set;
// import java.util.stream.Collectors;

// @Service
// public class FileService {
//     private static final String UPLOAD_DIR = "uploads/";
    
//     @Autowired
//     private FileRecordRepository fileRecordRepository;

//     public Set<String> findUniqueNumbers(Path file1, Path file2) throws IOException {
//         Set<String> numbers1 = new HashSet<>(Files.readAllLines(file1));
//         Set<String> numbers2 = new HashSet<>(Files.readAllLines(file2));
//                 // System.out.println("File1 Numbers: " + numbers1);
//         // System.out.println("File2 Numbers: " + numbers2);

//         Set<String> uniqueNumbers = new HashSet<>(numbers1);
//         // uniqueNumbers.addAll(numbers2);
        
//         // Set<String> commonNumbers = new HashSet<>(numbers1);
//         // commonNumbers.retainAll(numbers2);
        
//         uniqueNumbers.removeAll(numbers2); // Remove common numbers
        
//         return uniqueNumbers;
//     }



    
// }


package com.example.unique.services;

import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class FileService {

    public Path findUniqueNumbers(Path file1, Path file2, Path outputPath) throws IOException {
        // Use HashSet for file2 since we need quick lookups
        Set<String> numbers2 = new HashSet<>();

        // Read file2 line by line and store in HashSet (smallest file should be chosen)
        try (Stream<String> stream = Files.lines(file2)) {
            stream.forEach(numbers2::add);
        }

        // Process file1 line by line and write unique numbers directly to output file
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath);
             Stream<String> stream = Files.lines(file1)) {
            
            stream.filter(line -> !numbers2.contains(line)) // Only keep unique numbers
                  .forEach(line -> {
                      try {
                          writer.write(line);
                          writer.newLine();
                      } catch (IOException e) {
                          throw new RuntimeException(e);
                      }
                  });
        }

        return outputPath;
    }
}
