package me.drakeet.mailotto;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Test;

public class MailboxTest extends TestCase {

    private static final String CONTENT = "Send immediately.";
    private static final String MAILBOX_IDENTIFIER = "test-mailbox";

    private Mailbox mailbox;


    @Override protected void setUp() throws Exception {
        super.setUp();
        mailbox = new Mailbox(ThreadEnforcer.ANY, MAILBOX_IDENTIFIER);
        mailbox.atHome(this);
    }


    @OnMailReceived public void onDearMailRecevied(Mail mail) {
        System.out.println("on subscribe.");
        assertNotNull(mail);
        assertEquals(CONTENT, mail.content);
    }


    @Test public void testPostMail() {
        mailbox.post(new Mail(CONTENT, this.getClass()));
    }


    @After public void after() {
        mailbox.leave(this);
    }
}