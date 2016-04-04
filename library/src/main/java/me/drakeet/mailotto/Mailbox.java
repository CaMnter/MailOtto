package me.drakeet.mailotto;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
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
    private final ConcurrentMap<Class<?>, MailHandler> currentAtHomeHandlerByClass
            = new ConcurrentHashMap<>();

    private volatile static Mailbox instance = null;

    private final String identifier;
    private final ThreadEnforcer enforcer;
    private final HandlerFinder handlerFinder;

    private final ThreadLocal<LinkedList<Mail>> mailsToDispatch
            = new ThreadLocal<LinkedList<Mail>>() {
        @Override protected LinkedList<Mail> initialValue() {
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

        MailHandler onMailReceived = handlerFinder.findOnMailReceived(object);
        if (onMailReceived != null) {
            currentAtHomeHandlerByClass.put(object.getClass(), onMailReceived);
        }
        dispatchMails(object, onMailReceived);
    }


    public void leave(Object object) {
        if (object == null) {
            throw new NullPointerException("Object to leave must not be null.");
        }
        enforcer.enforce(this);

        MailHandler foundHandler = handlerFinder.findOnMailReceived(object);
        MailHandler cacheHandler = getCacheCurrentAtHomeHandler(object.getClass());
        if (cacheHandler == null || !cacheHandler.equals(foundHandler)) {
            throw new IllegalArgumentException(
                    "Missing mail handler for an annotated method. Is " + object.getClass() +
                            " atHome?");
        }

        if (foundHandler.equals(cacheHandler)) {
            cacheHandler.invalidate();
            currentAtHomeHandlerByClass.remove(object.getClass());
        }
    }


    MailHandler getCacheCurrentAtHomeHandler(Class<?> type) {
        return currentAtHomeHandlerByClass.get(type);
    }


    public void post(Mail mail) {
        if (mail == null) {
            throw new NullPointerException("Mail to post must not be null.");
        }
        enforcer.enforce(this);
        Class<?> toClass = mail.to;

        MailHandler handler = getCacheCurrentAtHomeHandler(toClass);
        if (handler != null) {
            dispatch(mail, handler);
        } else {
            enqueue(mail);
        }
    }


    protected void enqueue(Mail mail) {
        mailsToDispatch.get().add(mail);
    }


    private void dispatchMails(Object to, MailHandler onMailReceived) {
        if (isDispatching.get()) {
            return;
        }
        isDispatching.set(true);
        for (Mail mail : mailsToDispatch.get()) {
            if (mail.to == to.getClass()) {
                if (onMailReceived.isValid()) {
                    dispatch(mail, onMailReceived);
                }
                mailsToDispatch.get().remove(mail);
            }
        }
        isDispatching.set(false);
    }


    protected void dispatch(Mail mail, MailHandler wrapper) {
        try {
            wrapper.handleEvent(mail);
        } catch (InvocationTargetException e) {
            throwRuntimeException(
                    "Could not dispatch mail: " + mail.getClass() + " to handler " + wrapper, e);
        }
    }


    private static void throwRuntimeException(String msg, InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            throw new RuntimeException(msg + ": " + cause.getMessage(), cause);
        } else {
            throw new RuntimeException(msg + ": " + e.getMessage(), e);
        }
    }


    @Override public String toString() {
        return "[Mailbox \"" + identifier + "\"]";
    }
}
