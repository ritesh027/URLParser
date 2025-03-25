package com.ritesh.demo.urlfeederservice.dao;

import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.ritesh.demo.urlfeederservice.model.URL;

// public interface URLRepository extends JpaRepository<URL, String> {

//     @Query("SELECT u FROM URL u WHERE u.url = ?1")
//     URL findByUrl(String url);

// }

@Repository
public interface URLRepository extends CassandraRepository<URL, String>{

    @AllowFiltering
    Optional<URL> findByUrl(String url);
}
