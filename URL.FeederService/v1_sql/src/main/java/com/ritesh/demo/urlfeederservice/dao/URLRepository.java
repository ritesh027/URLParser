package com.ritesh.demo.urlfeederservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ritesh.demo.urlfeederservice.model.URL;

public interface URLRepository extends JpaRepository<URL, String> {

    @Query("SELECT u FROM URL u WHERE u.url = ?1")
    URL findByUrl(String url);

}
