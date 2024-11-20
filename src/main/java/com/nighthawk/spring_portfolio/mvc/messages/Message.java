package com.nighthawk.spring_portfolio.mvc.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;


import jakarta.persistence.*;

@Entity
@Data  // Annotations to simplify writing code (ie constructors, setters)
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Comparable<Message> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String content;


    @JsonManagedReference
    @OneToMany(mappedBy = "message", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    // Additional constructor if you want to set content only
    public Message(String content) {
        this.content = content;
        this.comments = new ArrayList<>(); // Ensure comments is initialized
    }

    /** Custom getter to return number of comments 
    */
    public int getNumberOfComment() {
        if (this.comments != null) {
            return comments.size();
        }
        return 0;
    }

    @Override
    public int compareTo(Message other){
        return this.content.compareTo(other.content);
    }


     /** 2nd telescoping method to create a Message object with parameterized comments
     * @param comments 
     */
    public static Message createMessage(String content, String reply) {
        Message message = new Message();
        message.setContent(content);
    
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setContent(reply);
        comments.add(comment);
        message.setComments(comments);
    
        return message;
    }
       
    /** Static method to initialize an array list of Message objects 
     * @return Message[], an array of Message objects
     */
    public static Message[] init() {
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(createMessage("whats your favorite color", "red"));
        messages.add(createMessage("What is an object in java", "String"));
        return messages.toArray(new Message[0]);
    }

      /** Static method to print Message objects from an array
     * @param args, not used
     */
    public static void main(String[] args) {
        // obtain Message from initializer
        Message messages[] = init();

        // iterate using "enhanced for loop"
        for( Message message : messages) {
            System.out.println(message);  // print object
        }
    }
}
