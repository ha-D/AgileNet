package models;

import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void getRateTest(){
        Comment comment = new Comment();
        assertTrue(comment.getRate()==0);
        comment.upvotes.add(new User());
        comment.upvotes.add(new User());
        comment.upvotes.add(new User());
        assertTrue(comment.getRate() == 3);
        comment.upvotes.remove(2);
        assertTrue(comment.getRate() == 2);

    }
}
