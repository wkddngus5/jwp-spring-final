package next.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import next.CannotOperateException;

public class AnswerTest {
	Answer answer = new Answer("writer", "contents", 3);
	
	@Test(expected=CannotOperateException.class)
	public void 답변과_작성자가_다를_때() throws CannotOperateException {
		User user = new User("diffrentWriter", "password", "writer", "email");
		
		answer.delete(user); 
		assertFalse(answer.isDeleted());
	}
	
	@Test
	public void 답변과_작성자가_같을_때() throws CannotOperateException {
		User user = new User("writer", "password", "writer", "email");
		
		answer.delete(user); 
		assertTrue(answer.isDeleted());
	}
	

}
