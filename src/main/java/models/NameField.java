package models;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class NameField {
    String name;
    int length;

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void writeToBuffer(ByteBuffer buffer) {
        buffer.put((byte) length);
        buffer.put(name.getBytes(StandardCharsets.UTF_8));
    }
}
