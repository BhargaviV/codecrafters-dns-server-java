package models;

import java.nio.ByteBuffer;
import java.util.List;

public class DNSPacket {

    Header header;
    List<Question> questions = List.of();
    List<Answer> answers = List.of();

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void writeToBuffer(ByteBuffer buffer) {
        header.writeToBuffer(buffer);
        for (Question question: questions) {
            question.writeToBuffer(buffer);
        }
        for (Answer answer: answers) {
            answer.writeToBuffer(buffer);
        }
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
