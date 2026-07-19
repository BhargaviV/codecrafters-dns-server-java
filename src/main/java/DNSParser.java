import models.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DNSParser {

    private RData resolveData(ByteBuffer buffer) {

        return new ARecord("192.32.10.0");
    }

    private List<NameField> readName(ByteBuffer buffer) {
        List<NameField> nameFields = new ArrayList<>();
        int value = buffer.get() & 0xFF;
        while (value != 0) {
            System.out.println("readName value" + value);
            NameField nameField = new NameField();
            nameField.setLength(value);
            byte[] data = new byte[value];
            buffer.get(data);
            value = buffer.get() & 0xFF;
            String label = new String(data, StandardCharsets.UTF_8);
            if (!label.isEmpty()) {
                nameField.setName(label);
                System.out.println("readName=" + label + ":" + value);
                nameFields.add(nameField);
            }
        }
        return nameFields;
    }


    DNSPacket parseUDP(ByteBuffer buffer) {
        DNSPacket dnsPacket = new DNSPacket();

        Header header = new Header();
        header.setId(buffer.getShort());
        header.setFlags(buffer.getShort());
        header.setQuestion_count(buffer.getShort());
        header.setAnswer_count(buffer.getShort());
        header.setAuth_count(buffer.getShort());
        header.setAdditional_count(buffer.getShort());

        dnsPacket.setHeader(header);

        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < header.getQuestion_count(); i++) {
            Question question = new Question();
            question.setNames(readName(buffer));
            question.setType(buffer.getShort());
            question.setClass_bit(buffer.getShort());

            System.out.println("header" + header.getId() + "questionType:" + question.getType());
            questions.add(question);
        }

        header.setQuestion_count(questions.size());
        dnsPacket.setQuestions(questions);

        List<Answer> answers = new ArrayList<>();
        for (Question question: questions) {
            Answer answer = new Answer();
            answer.setNames(question.getNames());
            answer.setType(1);
            answer.setClass_bit(buffer.getShort());
            answer.setTtl(buffer.getInt());
            answer.setLength(buffer.getShort());
            answer.setData(resolveData(buffer));
            answers.add(answer);
        }

        header.setAnswer_count(answers.size());
        dnsPacket.setAnswers(answers);
        return dnsPacket;
    }
}
