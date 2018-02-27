package main.models;

public class Chat {
    private String senderMail;
    private String receiverMail;
    private Message message;

    public String getSenderMail() {
        return senderMail;
    }

    public void setSenderMail(String senderMail) {
        this.senderMail = senderMail;
    }

    public String getReceiverMail() {
        return receiverMail;
    }

    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    public Chat(){};
    public Chat(String senderMail, String receiverMail, Message message) {
        super();
        this.senderMail = senderMail;
        this.receiverMail = receiverMail;
        this.message = message;
    }
}
