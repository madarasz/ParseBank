package com.madarasz.parsebank.database;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by imadaras on 12/6/15.
 */
public interface CategoryRepository extends GraphRepository<Category>, RelationshipOperationsRepository<Category> {

    @Query("MATCH (c:Category) RETURN c")
    List<Category> getAll();

    Category findById(Long id);

    Category findByTitle(String title);
}
