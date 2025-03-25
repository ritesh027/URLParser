package com.ritesh.demo.urlfeederservice.model;

import java.io.Serializable;
import java.sql.Timestamp;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

// @Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table("url")
public class URL implements Serializable{
    
    @PrimaryKeyColumn(name="id", ordinal=0, type=PrimaryKeyType.PARTITIONED)
    String id;

    @PrimaryKeyColumn(name="url", ordinal=1, type=PrimaryKeyType.PARTITIONED)
    String url;

    @Column("content_type")
    String ContentType;

    @Column("times_processed")
    Integer TimesProcessed;

    @Column("last_modified")
    Timestamp LastModified;

    @Column("created_date")
    Timestamp CreatedDate;
    
}
