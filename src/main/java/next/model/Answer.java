package next.model;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.CannotOperateException;

public class Answer {
	private static final Logger log = LoggerFactory.getLogger(Answer.class);

	private long answerId;
	
	private String writer;
	
	private String contents;
	
	private Date createdDate;

	private long questionId;
	
	private boolean deleted = false;
	
	public Answer(String writer, String contents, long questionId) {
		this(0, writer, contents, new Date(), questionId);
	}
	
	public Answer(long answerId, String writer, String contents, Date createdDate, long questionId) {
		this.answerId = answerId;
		this.writer = writer;
		this.contents = contents;
		this.createdDate = createdDate;
		this.questionId = questionId;
	}
	
	public long getAnswerId() {
		return answerId;
	}
	
	public String getWriter() {
		return writer;
	}

	public String getContents() {
		return contents;
	}

	public Date getCreatedDate() {
		return createdDate;
	}
	
	public long getTimeFromCreateDate() {
		return this.createdDate.getTime();
	}
	
	public long getQuestionId() {
		return questionId;
	}
	
	public boolean isSameUser(User user) {
		if (user == null) {
			return false;
		}
		return user.isSameUser(this.writer);
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (answerId ^ (answerId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Answer other = (Answer) obj;
		if (answerId != other.answerId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Answer [answerId=" + answerId + ", writer=" + writer
				+ ", contents=" + contents + ", createdDate=" + createdDate
				+ ", questionId=" + questionId + "]";
	}

	public void delete(User user) throws CannotOperateException {
		if (!isSameUser(user)) {
			throw new CannotOperateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}
		this.deleted = true;
	}
}
