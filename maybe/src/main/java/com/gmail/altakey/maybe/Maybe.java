package com.gmail.altakey.maybe;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.List;
import java.util.LinkedList;

/**
 * Value wrapper for safely handling @Nullable values for Android.
 *
 * @author Takahiro Yoshimura {@literal <altakey@gmail.com>}
 * @param <T> Type of value
 */
public class Maybe<T> {
    private final T mTarget;

    private Maybe(@NonNull final T o) {
        mTarget = o;
    }

    /**
     * Constructs Maybe with the given value.
     * 
     * @param <T> Type of value
     * @param o Value
     * @return Maybe
     */
    @NonNull
    public static <T> Maybe<T> of(@Nullable final T o) {
        return new Maybe<>(o);
    }

    /**
     * Constructs Maybe with the given value, declaring it is not a null.  Throws Maybe.Nothing if an null value is given.
     * 
     * @param <T> Type of value
     * @param o Value
     * @return Maybe
     * @throws Maybe.Nothing if a null is given
     */
    @NonNull
    public static <T> Maybe<T> fromJust(@NonNull final T o) throws Nothing {
        if (o != null) {
            return new Maybe<>(o);
        } else {
            throw new Nothing();
        }
    }

    /**
     * Constructs Maybe with a null value.
     * 
     * @param <T> Type of value
     * @return Maybe
     */
    @NonNull
    public static <T> Maybe<T> fromNothing() {
        return new Maybe<T>(null);
    }

    /**
     * Returns the value if it is not a null.  Throws Maybe.Nothing otherwise.
     * 
     * @return Value
     * @throws Maybe.Nothing if the value is null
     */
    @NonNull
    public T just() throws Nothing {
        if (isJust()) {
            return mTarget;
        } else {
            throw new Nothing();
        }
    }

    /**
     * Returns true if the value is not a null.
     * 
     * @return True if non-null, false otherwise
     */
    @NonNull
    public boolean isJust() {
        return mTarget != null;
    }

    /**
     * Returns true if the value is null.
     * 
     * @return True if non-null, false otherwise
     */
    @NonNull
    public boolean isNothing() {
        return !isJust();
    }

    /**
     * Runs the given continuation with the value, only if it is not a null.
     *
     * @param c Continuation
     * @return itself
     */
    @NonNull
    public Maybe<T> just(@NonNull final Continuation1<T> c) {
        if (isJust()) {
            c.run(mTarget);
        }
        return this;
    }

    /**
     * Runs the given continuation, if the value is a null.
     *
     * @param c Continuation
     * @return itself
     */
    @NonNull
    public Maybe<T> nothing(@NonNull final Continuation0 c) {
        if (isNothing()) {
            c.run();
        }
        return this;        
    }
        
    /**
     * Concatenates the non-null values from the given Maybes.
     *
     * @param <T> Type of value
     * @param l List of Maybe
     * @return List of non-null values
     */
    @NonNull
    public static <T> List<T> catMaybes(final List<Maybe<T>> l) {
        final List<T> o = new LinkedList<T>();
        final Continuation1<T> adder = new Continuation1<T>() {
            @Override
            public void run(T e) {
                o.add(e);
            }
        };
        for (Maybe<T> m : l) {
            m.just(adder);
        }
        return o;
    }

    /**
     * Returns mapped values the non-null values from the given Maybes, with the given Mapper.
     *
     * @param <T> Type of value
     * @param mapper Mapper
     * @param l List of Maybe
     * @return List of non-null values
     */
    @NonNull
    public static <T> List<T> mapMaybes(final Mapper<T> mapper, final List<Maybe<T>> l) {
        final List<T> o = new LinkedList<T>();
        final Continuation1<T> adder = new Continuation1<T>() {
            @Override
            public void run(T e) {
                try {
                    o.add(mapper.mapped(e).just());
                } catch (final Nothing ignore) {
                }
            }
        };
        for (Maybe<T> m : l) {
            m.just(adder);
        }
        return o;
    }


    /**
     * Continuation without parameters.
     */
    public interface Continuation0 {
        void run();
    }

    /**
     * Continuation with one parameter.
     */
    public interface Continuation1<T> {
        void run(T o);
    }

    /**
     * Mapper.
     */
    public interface Mapper<T> {
        Maybe<T> mapped(T o);
    }

    /**
     * Exception class for indicating broken contracts on nullity.
     */
    public static class Nothing extends Exception {
    }
}
