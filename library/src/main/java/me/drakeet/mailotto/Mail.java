package me.drakeet.mailotto;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 16/3/27 21:37
 */
public class Mail {

    public Class<?> from;
    public Class<?> to;
    public Object content;


    public Mail(Class<?> to, Object content) {
        this(null, to, content);
    }


    public Mail(Class<?> from, Class<?> to, Object content) {
        this.to = to;
        this.from = from;
        this.content = content;
    }
}
