package com.nighthawk.spring_portfolio.mvc.Grades;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.CascadeType;
import org.springframework.http.HttpStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.nighthawk.spring_portfolio.mvc.Grades.Assignment;
import com.nighthawk.spring_portfolio.security.JwtTokenUtil;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentApiController {
    
    @Autowired
    private AssignmentJpaRepository repository;
    
    private JwtTokenUtil jwtTokenUtil;
    //jwtTokenUtil.


    @PostMapping("/add")
    public ResponseEntity<Assignment> addAssignment(@RequestBody Assignment assignmentName) {

        Assignment savedAssignment = repository.save(assignmentName);
        return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED); 
    }



}
