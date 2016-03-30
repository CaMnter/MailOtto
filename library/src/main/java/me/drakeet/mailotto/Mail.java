package me.drakeet.mailotto;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 16/3/27 21:37
 */
public class Mail {

    public Class<?> from;
    public Class<?> to;
    public Object content;


    public Mail(Object content, Class<?> to) {
        this(content, to, null);
    }


    public Mail(Object content, Class<?> to, Class<?> from) {
        this.to = to;
        this.from = from;
        this.content = content;
    }
}
