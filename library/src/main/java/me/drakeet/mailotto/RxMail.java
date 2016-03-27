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

    private static RxMail sMail;

    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());


    public static synchronized RxMail getInstance() {
        if (sMail == null) {
            sMail = new RxMail();
        }
        return sMail;
    }


    public void checkMails(Object o) {
        checkMails(o.getClass());
    }


    public Observable<Object> toObserverable() {
        return _bus;
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


    private void sendMail(Mail mail) {
        if (_bus.hasObservers()) {
            _bus.onNext(mail);
            if (mMails.contains(mail)) {
                mMails.remove(mail);
            }
        } else if (!mMails.contains(mail)) {
            mMails.add(mail);
        } else {
            // pass
        }
    }


    public void send(Mail mail) {
        sendMail(mail);
    }
}
