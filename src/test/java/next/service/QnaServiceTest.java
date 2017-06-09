package next.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static io.restassured.RestAssured.*;

import java.util.Date;

import next.CannotOperateException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
	private static final Logger log = LoggerFactory.getLogger(QnaServiceTest.class);

	@Mock
    private QuestionDao questionDao;
    @Mock
    private AnswerDao answerDao;
    
    private QnaService qnaService;
    User loginUser = new User("s", "s", "자바지기", "admin@slipp.net");

    @Before
    public void setup() {
    	RestAssured.port = 8080;
    	qnaService = new QnaService(questionDao, answerDao);
    }
    
    @Test
    public void create() throws Exception {
      given()
          .auth().preemptive().basic(loginUser.getUserId(), loginUser.getPassword())
          .contentType(ContentType.HTML)
      .when()
          .post("/")
      .then()
          .statusCode(200);
    }
    
    @Test
    public void delete() {
    	
    	String body = given()
    		.auth().preemptive().basic(loginUser.getUserId(), loginUser.getPassword())
    		.contentType(ContentType.JSON)
    	.when()
    		.delete("/questions/11")
    	.then()
    		.statusCode(200)
    		.extract()
    		.asString();
    	log.debug("body: {}", body);
    }
    
    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteQuestion_없는_질문() throws Exception {
        when(questionDao.findById(1L)).thenReturn(null);
        qnaService.deleteQuestion(1L, newUser("userId"));
    }
    
    @Test(expected = CannotOperateException.class)
    public void deleteQuestion_다른_사용자() throws Exception {
    	Question question = newQuestion(1L, "javajigi");
        when(questionDao.findById(1L)).thenReturn(question);
        when(answerDao.findAllByQuestionId(1L)).thenReturn(Lists.newArrayList());
        qnaService.deleteQuestion(1L, newUser("sanjigi"));
    }

    @Test
    public void deleteQuestion_같은_사용자_답변없음() throws Exception {
    	Question question = newQuestion(1L, "javajigi");
        when(questionDao.findById(1L)).thenReturn(question);
        when(answerDao.findAllByQuestionId(1L)).thenReturn(Lists.newArrayList());
        qnaService.deleteQuestion(1L, newUser("javajigi"));
        verify(questionDao).delete(1L);
    }
    
    @Test
    public void deleteQuestion_질문_답변_글쓴이_같음() throws Exception {
    	String userId = "javajigi";
		Question question = newQuestion(1L, userId);
        when(questionDao.findById(1L)).thenReturn(question);
        when(answerDao.findAllByQuestionId(1L)).thenReturn(Lists.newArrayList(newAnswer(userId), newAnswer(userId)));
        qnaService.deleteQuestion(1L, newUser(userId));
        verify(questionDao).delete(1L);
    }
    
    @Test(expected = CannotOperateException.class)
    public void deleteQuestion_질문_답변_글쓴이_다름() throws Exception {
    	String userId = "javajigi";
		Question question = newQuestion(1L, userId);
        when(questionDao.findById(1L)).thenReturn(question);
        when(answerDao.findAllByQuestionId(1L)).thenReturn(Lists.newArrayList(newAnswer(userId), newAnswer("sanjigi")));
        qnaService.deleteQuestion(1L, newUser(userId));
    }

    private User newUser(String userId) {
		return new User(userId, "password", "name", "test@sample.com");
	}
    
    private Question newQuestion(long questionId, String userId) {
    	return new Question(questionId, userId, "title", "contents", new Date(), 0);
    }
    
    private Answer newAnswer(String userId) {
		return new Answer(userId, "contents", 3L);
	}
}
