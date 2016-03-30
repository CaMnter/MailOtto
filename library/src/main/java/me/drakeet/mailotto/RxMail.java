package me.drakeet.mailotto;

import java.util.HashSet;
import java.util.Set;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 16/3/27 21:31
 */
public class RxMail {

    private Set<Mail> mMails = new HashSet<>();
    private Set<Class<?>> mHandlers = new HashSet<>();

    private final Subject<Object, Object> _postman = new SerializedSubject<>(
            PublishSubject.create());

    private volatile static RxMail instance = null;


    public static RxMail getInstance() {
        if (instance == null) {
            synchronized (RxMail.class) {
                if (instance == null) instance = new RxMail();
            }
        }
        return instance;
    }


    public Observable<Object> toObserverable() {
        return _postman;
    }


    public boolean checkMails(Object o) {
        return checkMails(o.getClass());
    }


    private boolean checkMails(Class<?> handler) {
        Set<Mail> mails = new HashSet<>();
        for (Mail mail : mMails) {
            if (mail.to == handler) {
                mails.add(mail);
            }
        }
        if (mails.size() > 0) {
            for (Mail mail : mails) {
                sendMail(mail);
            }
            return true;
        }
        return false;
    }


    /**
     * Send mails.
     *
     * @param mails mails
     * @return if all of the mails are sent to their observers, return true, otherwise return false.
     */
    public boolean send(Mail... mails) {
        boolean result = true;
        for (Mail mail : mails) {
            result &= sendMail(mail);
        }
        return result;
    }


    private boolean sendMail(Mail mail) {
        if (_postman.hasObservers()) {
            _postman.onNext(mail);
            if (mMails.contains(mail)) {
                mMails.remove(mail);
            }
            return true;
        } else if (!mMails.contains(mail)) {
            mMails.add(mail);
        } else {
            // pass
        }
        return false;
    }
}
