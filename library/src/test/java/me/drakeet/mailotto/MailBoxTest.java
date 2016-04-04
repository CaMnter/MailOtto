package me.drakeet.mailotto;

import junit.framework.TestCase;
import org.junit.Test;
import rx.Subscription;
import rx.functions.Action1;

public class MailboxTest extends TestCase {

    private static final String CONTENT = "Send immediately.";
    private Subscription mSubscription;


    @Override protected void setUp() throws Exception {
        super.setUp();
        mSubscription = Mailbox.getInstance().toObserverable().subscribe(new Action1<Object>() {
            @Override public void call(Object o) {
                System.out.println("on subscribe.");
                assertNotNull(o);
                assertTrue(o instanceof Mail);
                assertEquals(CONTENT, ((Mail) o).content);
            }
        });
        boolean result = Mailbox.getInstance().send(new Mail(CONTENT, this.getClass()));
        assertTrue(result);
    }


    /**
     * The send method will send the mail to its observer immediately if there is a observer,
     * so it will return false when you check mails after sending.
     */
    @Test public void testCheckMails() {
        assertFalse(Mailbox.getInstance().checkMails(this));

        mSubscription.unsubscribe();
        boolean result = Mailbox.getInstance().send(new Mail(CONTENT, this.getClass()));
        assertFalse(result);
        assertTrue(Mailbox.getInstance().checkMails(this));
        // again
        assertTrue(Mailbox.getInstance().checkMails(this));
    }


    @OnMailReceived public void onMailReveived(Mail mail) {
        if (mail.content instanceof String) {
            System.out.println("haha");
        }
    }
}