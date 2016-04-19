package me.drakeet.mailotto.mail;

import me.drakeet.mailotto.Mail;

/**
 * Description：PreloadMail
 * Created by：CaMnter
 * Time：2016-04-18 18:09
 */
public abstract class PreloadMail<T> extends Mail {

    public boolean preLoad;
    public T preData;
    public boolean loaded = false;


    public PreloadMail(Class<?> to) {
        this(to, true);
    }


    public PreloadMail(Class<?> to, boolean preLoad) {
        super(null, to);
        this.preLoad = preLoad;
    }


    public PreloadMail(Class<?> to, Class<?> from, boolean preLoad) {
        super(null, to, from);
        this.preLoad = preLoad;
    }


    public void executing() {
        if (this.preLoad) {
            this.preData = this.packaging();
            this.loaded = true;
        }
    }


    public abstract T packaging();
}
