package me.drakeet.mailotto;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 16/3/27 21:31
 *
 * @author Cliff Biffle
 * @author Jake Wharton
 * @author drakeet
 */
public class Mailbox {

    public static final String DEFAULT_IDENTIFIER = "default";

    /**
     * All registered mail handlers, indexed by mail type.
     */
    private final ConcurrentMap<Class<?>, MailHandler> handlerByType = new ConcurrentHashMap<>();

    private volatile static Mailbox instance = null;

    private final String identifier;
    private final ThreadEnforcer enforcer;
    private final HandlerFinder handlerFinder;

    private final ThreadLocal<LinkedList<MailWithHandler>> mailsToDispatch
            = new ThreadLocal<LinkedList<MailWithHandler>>() {
        @Override protected LinkedList<MailWithHandler> initialValue() {
            return new LinkedList<>();
        }
    };

    /**
     * True if the current thread is currently dispatching an event.
     */
    private final ThreadLocal<Boolean> isDispatching = new ThreadLocal<Boolean>() {
        @Override protected Boolean initialValue() {
            return false;
        }
    };


    public Mailbox() {
        this(DEFAULT_IDENTIFIER);
    }


    public Mailbox(String identifier) {
        this(ThreadEnforcer.MAIN, identifier);
    }


    public Mailbox(ThreadEnforcer enforcer) {
        this(enforcer, DEFAULT_IDENTIFIER);
    }


    public Mailbox(ThreadEnforcer enforcer, String identifier) {
        this(enforcer, identifier, HandlerFinder.ANNOTATED);
    }


    Mailbox(ThreadEnforcer enforcer, String identifier, HandlerFinder handlerFinder) {
        this.enforcer = enforcer;
        this.identifier = identifier;
        this.handlerFinder = handlerFinder;
    }


    public static Mailbox getInstance() {
        if (instance == null) {
            synchronized (Mailbox.class) {
                if (instance == null) instance = new Mailbox();
            }
        }
        return instance;
    }


    public void atHome(Object object) {
        if (object == null) {
            throw new NullPointerException("Object to atHome must not be null.");
        }
        enforcer.enforce(this);

        Map<Class<?>, MailHandler> foundHandlerMap = handlerFinder.findAllSubscribers(object);
        for (Class<?> type : foundHandlerMap.keySet()) {

            // 拿到缓存 MailHandler 集合
            MailHandler handler = handlerByType.get(type);
            if (handler == null) {
                // TODO: 16/4/3
            }
        }
        dispatchMails(object);
    }


    public void leave(Object object) {
        if (object == null) {
            throw new NullPointerException("Object to leave must not be null.");
        }
        enforcer.enforce(this);

        Map<Class<?>, MailHandler> handlersInListener = handlerFinder.findAllSubscribers(object);
        for (Map.Entry<Class<?>, MailHandler> entry : handlersInListener.entrySet()) {
            MailHandler currentHandler = getHandlerForMailType(entry.getKey());
            MailHandler eventMethodsInListener = entry.getValue();

            if (currentHandler == null || !currentHandler.equals(eventMethodsInListener)) {
                throw new IllegalArgumentException(
                        "Missing event handler for an annotated method. Is " + object.getClass() +
                                " registered?");
            }

            if (eventMethodsInListener.equals(currentHandler)) {
                currentHandler.invalidate();
            }
        }
    }


    MailHandler getHandlerForMailType(Class<?> type) {
        return handlerByType.get(type);
    }


    public void post(Mail mail) {
        if (mail == null) {
            throw new NullPointerException("Mail to post must not be null.");
        }
        enforcer.enforce(this);
        Class<?> mailType = mail.getClass();

        MailHandler handler = getHandlerForMailType(mailType);
        if (handler != null) {
            dispatch(mail, handler);
            mailsToDispatch.get().remove(new MailWithHandler(mail, handler));
        } else {
            // TODO: 16/4/3 should check null when poll
            enqueueMail(mail, handler);
        }
    }


    protected void dispatch(Object event, MailHandler wrapper) {
        try {
            wrapper.handleEvent(event);
        } catch (InvocationTargetException e) {
            throwRuntimeException(
                    "Could not dispatch mail: " + event.getClass() + " to handler " + wrapper, e);
        }
    }


    protected void enqueueMail(Mail mail, MailHandler handler) {
        mailsToDispatch.get().add(new MailWithHandler(mail, handler));
    }


    private static void throwRuntimeException(String msg, InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            throw new RuntimeException(msg + ": " + cause.getMessage(), cause);
        } else {
            throw new RuntimeException(msg + ": " + e.getMessage(), e);
        }
    }


    private void dispatchMails(Object to) {

        if (isDispatching.get()) {
            return;
        }

        isDispatching.set(true);
        for (MailWithHandler mh : mailsToDispatch.get()) {
            if (mh.mail.to == to.getClass()) {
                if (mh.handler.isValid()) {
                    dispatch(mh.mail, mh.handler);
                }
                mailsToDispatch.get().remove(mh);
            }
        }
        isDispatching.set(false);
    }



    @Override public String toString() {
        return "[Mailbox \"" + identifier + "\"]";
    }
}
