# maybe: Maybe-like wrapper for Android

One approach for eliminating null checks.

Null checks are "if" statements testing values for the null conditions.  For example:

```java
if (x != null) {
    // success
} else {
    // failure (ugh)
}
```

Null checks are an important source of bugs because they are error-prone.  They are error-prone because they are entirely optional.  Programmers are not only free to silently return nulls for one reason or another, but also free (well, _tend_) to forget null checks.

**Optional** is a class that intends to address the problem.  It is available under Java 8 or better.  Unfortunately Android does _not_ support Java 8 _yet_.

Hence, **maybe**.

## Concept

**maybe** is an immutable value wrapper class that works similar to Optional.  It wraps one value and perform nullity checks upon usage.  If a null value is detected upon a non-null value is expected, the Maybe.Nothing exception will be thrown.  The Maybe.Nothing exception is a checked exception so that you cannot forget handling it.

**maybe** is modelled after the Data.Maybe in Haskell.

## Quickstart

First import the class:

```java
import com.gmail.altakey.maybe.Maybe;
```

Then you can write as follows:

```java
public class SomeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            final Intent intent = Maybe.of(getIntent()).just();

            // intent is not null
            switch (intent.getAction()) {
            case Intent.ACTION_VIEW:
                // do something
            }
        } catch (Maybe.Nothing e) {
            // intent is null   
        }
    }
    ...
}   
```

With retrolambda, you can write:

```java
public class SomeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      Maybe.of(getIntent()).just(intent -> {
        // launched with intent
        switch (intent.getAction()) {
        case Intent.ACTION_VIEW:
          // do something
        }
        }).nothing(() -> {
          // system restarted our activity
        });
    }
    ...
}
```

Cool, isn't it?  Feel free to read the [Javadoc](https://jitpack.io/com/github/taky/maybe/-SNAPSHOT/javadoc/).

## Download

**maybe** uses https://jitpack.io for packaging.

jitpack.io requires you to setup their repository first, then specify dependency.  Hence in Maven:

```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
  
<dependency>
  <groupId>com.github.taky</groupId>
  <artifactId>maybe</artifactId>
  <version>-SNAPSHOT</version>
</dependency>
```

or in Gradle:
```
allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
}
```
and 
```
dependencies {
        compile 'com.github.taky:maybe:-SNAPSHOT'
}
```

## License

The MIT License (MIT)

```
Copyright (c) 2016 Takahiro Yoshimura

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
