package com.netply.botchan.web.model;

public class Message {
    private final long id;
    private final String message;
    private final String sender;


    public Message(long id, String message) {
        this(id, message, null);
    }

    public Message(long id, String message, String sender) {
        this.id = id;
        this.message = message;
        this.sender = sender;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (id != message1.id) return false;
        if (message != null ? !message.equals(message1.message) : message1.message != null) return false;
        return sender != null ? sender.equals(message1.sender) : message1.sender == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}
