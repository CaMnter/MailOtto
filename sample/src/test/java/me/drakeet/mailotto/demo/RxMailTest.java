package me.drakeet.mailotto.demo;

import junit.framework.TestCase;
import me.drakeet.mailotto.Mail;
import me.drakeet.mailotto.RxMail;
import org.junit.Test;
import rx.Subscription;
import rx.functions.Action1;


public class RxMailTest extends TestCase {

    private static final String CONTENT = "Send immediately.";
    private Subscription mSubscription;


    @Override protected void setUp() throws Exception {
        super.setUp();
        mSubscription = RxMail.getInstance().toObserverable().subscribe(new Action1<Object>() {
            @Override public void call(Object o) {
                System.out.println("on subscribe.");
                assertNotNull(o);
                assertTrue(o instanceof Mail);
                assertEquals(CONTENT, ((Mail) o).content);
            }
        });
        boolean result = RxMail.getInstance().send(new Mail(CONTENT, this.getClass()));
        assertTrue(result);
    }



    /**
     * The send method will send the mail to their observers immediately if there is a observer,
     * so it will return false when you check mails after sending.
     */
    @Test public void testCheckMails() {
        assertFalse(RxMail.getInstance().checkMails(this));

        mSubscription.unsubscribe();
        boolean result = RxMail.getInstance().send(new Mail(CONTENT, this.getClass()));
        assertFalse(result);
        assertTrue(RxMail.getInstance().checkMails(this));
        // again
        assertTrue(RxMail.getInstance().checkMails(this));
    }
}