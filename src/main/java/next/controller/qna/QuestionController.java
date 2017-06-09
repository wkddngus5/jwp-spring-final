package next.controller.qna;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import core.web.argumentresolver.LoginUser;
import next.CannotOperateException;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import next.service.QnaService;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private QnaService qnaService;
	
	private Question question;
	
	private List<Answer> answers;

	@RequestMapping(value = "/{questionId}", method = RequestMethod.GET)
	public String show(@PathVariable long questionId, Model model) throws Exception {
	    question = qnaService.findById(questionId);
	    answers = qnaService.findAllByQuestionId(questionId);
	    
		model.addAttribute("question", question);
		model.addAttribute("answers", answers);
		return "/qna/show";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String createForm(@LoginUser User loginUser, Model model) throws Exception {
		if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
		model.addAttribute("question", new Question());
		return "/qna/form";
	}

	@RequestMapping(value = "/{questionId}", method = RequestMethod.POST)
	public String create(@LoginUser User loginUser, @PathVariable long questionId, Question question) throws Exception {
		if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
		if(questionId == 0) {
			questionDao.insert(question.newQuestion(loginUser));			
		}else {
			questionDao.update(question);
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/{questionId}", method = RequestMethod.DELETE)
	public String delete(@LoginUser User loginUser, @PathVariable long questionId, Model model) throws Exception {
		try {
			qnaService.deleteQuestion(questionId, loginUser);
			return "redirect:/";
		} catch (CannotOperateException e) {
			model.addAttribute("question", qnaService.findById(questionId));
			model.addAttribute("errorMessage", e.getMessage());
			return "show";
		}
	}
	
	@RequestMapping(value = "/{questionId}/edit", method = RequestMethod.GET)
	public String edit(@LoginUser User loginUser, @PathVariable long questionId, Model model) throws Exception {
		if (loginUser.isGuestUser()) {
			return "redirect:/users/loginForm";
		}
		model.addAttribute("question", qnaService.findById(questionId));
		return "/qna/form";
	}
}
