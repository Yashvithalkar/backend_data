package com.example.unique.entity;

// import javax.persistence.*;
import jakarta.*;
import jakarta.persistence.*;

@Entity
@Table(name = "file_records")
public class FileRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String fileName;
    
    public FileRecord() {}
    
    public FileRecord(String fileName) {
        this.fileName = fileName;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
