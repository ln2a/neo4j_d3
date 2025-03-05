package com.example.helloworld.controller;

import com.example.helloworld.pojo.CustomLink;
import com.example.helloworld.pojo.CustomNode;
import com.example.helloworld.pojo.Movie;
import com.example.helloworld.pojo.Person;
import com.example.helloworld.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping("/{name}")
    public CustomNode findByName(@PathVariable("name") String name){
//        return personService.findByName(name);
        Person person=personService.findByName(name);
        return new CustomNode(person.getName(), 1,person);
    }

//    找这个人所饰演的电影
    @GetMapping("/query/{relationship}/{name}")
    public List<CustomNode> getActedByName(@PathVariable("relationship") String rel,@PathVariable("name") String name){
        Person person=personService.findByName(name);
        Set<Movie> movies=new HashSet<>();
//        List<CustomLink> customLinks=new ArrayList<>();
        List<CustomNode> customNodes=new ArrayList<>();
        movies=personService.getMoviesByRelationship(person,rel);

        for(Movie movie:movies){
            customNodes.add(new CustomNode(movie.getTitle(),5,movie));
        }
        return customNodes;
    }

//    找所有对象 以node格式返回
    @GetMapping("/all")
    public List<CustomNode> findAll(){
        Iterable<Person> personIterable=personService.findAll();
        List<CustomNode> customNodes=new ArrayList<>();
        for(Person person:personIterable){
            customNodes.add(new CustomNode(person.getName(), 1,person));
        }
        return customNodes;
    }



}
