package me.drakeet.mailotto;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 16/4/3 21:08
 */
public class MailWithHandler {
    final Mail mail;
    final MailHandler handler;


    public MailWithHandler(Mail mail, MailHandler handler) {
        this.mail = mail;
        this.handler = handler;
    }


    @Override public boolean equals(Object o) {
        if (!(o instanceof MailWithHandler)) {
            return false;
        } else {
            MailWithHandler other = (MailWithHandler) o;
            return this.mail.equals(other.mail) && this.handler.equals(other.handler);
        }
    }
}
