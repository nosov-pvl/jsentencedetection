package ru.spbstu.jsentencedetection.loaders;

public class Message {
    private String body;
    private String subject;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "subject:\n" + subject + "\n" +
               "body:\n" + body;
    }
}
