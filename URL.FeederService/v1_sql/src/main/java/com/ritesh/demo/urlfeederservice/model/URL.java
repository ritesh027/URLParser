package com.ritesh.demo.urlfeederservice.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "urlCollections")
public class URL implements Serializable{
    
    @Id
    String id;

    String url;

    @Column(name = "content_type")
    String ContentType;

    @Column(name = "times_processed")
    Integer TimesProcessed;

    @Column(name = "last_processed")
    Timestamp LastProcessed;

    @CreatedDate
    @Column(name = "created_date")
    Timestamp CreatedDate;
    
}
