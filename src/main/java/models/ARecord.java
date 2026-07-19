package models;

public class ARecord implements RData {

    String ip;

    public ARecord(String ip) {
        this.ip = ip;
    }

    public Object getData() {
        return ip;
    }
}
