# MailOtto v1.0
A mail box.

Mail
--------

```java
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
```

Post mails
---------

```java
Mailbox.getInstance().post(mail);
```

AtHome, and the postman will give you the mails.
------------------------------------------------

```java
Mailbox.getInstance().atHome(this);
```

When received a mail
--------

```java
@OnMailReceived public void onDearMailReceived(Mail mail) {
    Toast.makeText(ConsumerActivity.this, 
                   mail.content.toString(), 
                   Toast.LENGTH_SHORT).show();
}
```

**Thanks to:**

[square/otto](https://github.com/square/otto)

License
-------

    Copyright 2016 drakeet.
    Copyright 2012 Square, Inc.
    Copyright 2010 Google, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [1]: http://square.github.com/otto/
 [2]: http://github.com/square/otto/downloads
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/