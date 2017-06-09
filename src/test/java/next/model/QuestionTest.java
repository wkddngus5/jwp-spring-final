package next.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import next.CannotOperateException;

public class QuestionTest {
	Question question = new Question("writer", "title", "contents");

	@Test
	public void 답변이_없을_때() throws CannotOperateException {
		User user = new User("writer", "password", "writer", "email");
		question.delete(user, new ArrayList<Answer>()); 
		assertTrue(question.isDeleted());
	}
	
	@Test(expected=CannotOperateException.class)
	public void 질문과_작성자가_다를_때() throws CannotOperateException {
		User user = new User("diffrentWriter", "password", "differnetWriter", "email");
		question.delete(user, new ArrayList<Answer>()); 
		assertFalse(question.isDeleted());
	}
}
