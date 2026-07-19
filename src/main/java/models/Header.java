package models;

import java.nio.ByteBuffer;

public class Header {

    static final int QR = 0x8000;
    static final int OPCODE = 0x7800;
    static final int AA = 0x0400;
    static final int TC = 0x0200;
    static final int RD = 0x0100;
    static final int RA = 0x0080;
    static final int RSCODE = 0x000F;

    // A random identifier is assigned to query packets. Response packets must reply with the same id.
    // This is needed to differentiate responses due to the stateless nature of UDP.
    int id;

    int flags;

    // Query Response 0 for queries, 1 for responses.
    int query_or_response;

    // Typically always 0, see RFC1035 for details.
    int opcode;

    // Set to 1 if the responding server is authoritative - that is, it "owns" - the domain queried.
    int auth_answer;

    // Set to 1 if the message length exceeds 512 bytes.
    // Traditionally a hint that the query can be reissued using TCP, for which the length limitation doesn't apply.
    int truncated_message;

    // Set by the sender of the request if the
    // server should attempt to resolve the query recursively if it does not have an answer readily available.
    int recursion_desired;

    // Set by the server to indicate whether or not recursive queries are allowed.
    int recursion_available;

    // Originally reserved for later use, but now used for DNSSEC queries.
    int reserved;

    // Set by the server to indicate the status of the response, i.e. whether or not it was successful or failed,
    // and in the latter case providing details about the cause of the failure.
    int response_code;

    // The number of entries in the Question Section
    int question_count;

    // The number of entries in the Answer Section
    int answer_count;

    // The number of entries in the Authority Section
    int auth_count;

    // The number of entries in the Additional Section
    int additional_count;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getAdditional_count() {
        return additional_count;
    }

    public void setAdditional_count(int additional_count) {
        this.additional_count = additional_count;
    }

    public int getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(int answer_count) {
        this.answer_count = answer_count;
    }

    public int getAuth_count() {
        return auth_count;
    }

    public void setAuth_count(int auth_count) {
        this.auth_count = auth_count;
    }

    public int getQuestion_count() {
        return question_count;
    }

    public void setQuestion_count(int question_count) {
        this.question_count = question_count;
    }

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getRecursion_available() {
        return recursion_available;
    }

    public void setRecursion_available(int recursion_available) {
        this.recursion_available = recursion_available;
    }

    public int getRecursion_desired() {
        return recursion_desired;
    }

    public void setRecursion_desired(int recursion_desired) {
        this.recursion_desired = recursion_desired;
    }

    public int getTruncated_message() {
        return truncated_message;
    }

    public void setTruncated_message(int truncated_message) {
        this.truncated_message = truncated_message;
    }

    public int getAuth_answer() {
        return auth_answer;
    }

    public void setAuth_answer(int auth_answer) {
        this.auth_answer = auth_answer;
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public int getQuery_or_response() {
        return query_or_response;
    }

    public void setQuery_or_response(int query_or_response) {
        this.query_or_response = query_or_response;
        if (query_or_response != 0) {
            this.flags |= QR;
        } else {
            this.flags &= ~QR;
        }
    }

    public void setFlags(int flags) {
        this.flags = flags;
        this.setQuery_or_response(flags & QR);
        this.setAuth_answer(flags & AA);
        this.setOpcode(flags & OPCODE);
        this.setTruncated_message(flags & TC);
        this.setRecursion_desired(flags & RD);
        this.setRecursion_available(flags & RA);
        this.setResponse_code(flags & RSCODE);
    }

    public void writeToBuffer(ByteBuffer buffer) {
        if (this.getOpcode() != 0) {
            flags &= ~0x000F;                  // Clear current RCODE
            flags |= (4 & 0x000F);          // Set new RCODE
        }

        buffer.putShort((short) id);
        buffer.putShort((short) flags);
        buffer.putShort((short) question_count);
        buffer.putShort((short) answer_count);
        buffer.putShort((short) auth_count);
        buffer.putShort((short) additional_count);
    }
}
