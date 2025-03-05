package com.example.helloworld.dao;

import com.example.helloworld.pojo.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PersonDao extends Neo4jRepository<Person,Long> {
   Person findByName(String name);

}
