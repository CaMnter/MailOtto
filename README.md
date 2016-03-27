# MailOtto v0.1
A mail box.

Mail
--------

```java
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
```

Send mail
--------

```java
RxMail.getInstance().send(mail);
```

Check mails
--------

```java
RxMail.getInstance().checkMails(this);
```

When received a mail
--------

```java
RxMail.getInstance().toObserverable().subscribe(new Action1<Object>() {
    @Override public void call(Object event) {

        if (event instanceof Mail) {
            Toast.makeText(this, ((Mail) event).content.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
});
```
