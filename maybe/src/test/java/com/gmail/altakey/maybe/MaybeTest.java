package com.gmail.altakey.maybe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class MaybeTest {
    @Test
    public void testOf() {
        assertTrue(Maybe.of("test") != null);
    }

    @Test
    public void testFromJust() throws Maybe.Nothing {
        assertTrue(Maybe.fromJust("test") != null);
    }

    @Test(expected=Maybe.Nothing.class)
    public void testFromJustOnNull() throws Maybe.Nothing {
        Maybe.fromJust(null);
    }

    @Test
    public void testFromNothing() {
        assertTrue(Maybe.fromNothing() != null);
    }

    @Test
    public void testIsJust() {
        assertTrue(Maybe.of("test").isJust());
        assertFalse(Maybe.of(null).isJust());
    }

    @Test
    public void testIsNothing() {
        assertTrue(Maybe.of("test").isJust());
        assertFalse(Maybe.of(null).isJust());
    }

    @Test
    public void testJust() throws Maybe.Nothing {
        final String corpse = "a";
        assertEquals(corpse, Maybe.of(corpse).just());
    }

    @Test(expected=Maybe.Nothing.class)
    public void testJustOnNull() throws Maybe.Nothing {
        Maybe.of(null).just();
    }

    @Test
    public void testJustContinuation() {
        final List<Integer> o = new ArrayList<>();

        Maybe.of("test").just(new Maybe.Continuation1<String>() {
            @Override
            public void run(String v) {
                o.add(1);
            }
        });
        Maybe.of((String)null).just(new Maybe.Continuation1<String>() {
            @Override
            public void run(String v) {
                o.add(2);
            }
        });
        assertListsEqual(Arrays.asList(Integer.valueOf(1)), o);
    }

    @Test
    public void testNothingContinuation() {
        final List<Integer> o = new ArrayList<>();

        Maybe.of("test").nothing(new Maybe.Continuation0() {
            @Override
            public void run() {
                o.add(1);
            }
        });
        Maybe.of((String)null).nothing(new Maybe.Continuation0() {
            @Override
            public void run() {
                o.add(2);
            }
        });
        assertListsEqual(Arrays.asList(Integer.valueOf(2)), o);
    }

    @Test
    public void testCatMaybes() {
        assertListsEqual(
            Arrays.asList("a", "b", "d"),
            Maybe.catMaybes(Arrays.asList(Maybe.of("a"), Maybe.of("b"), Maybe.of((String)null), Maybe.of("d")))
        );
    }

    @Test
    public void testMapMaybes() {
        assertListsEqual(
            Arrays.asList(2,4),
            Maybe.mapMaybes(
                new Maybe.Mapper<Integer, Integer>() {
                    @Override
                    public Maybe<Integer> mapped(Integer x) {
                        return (x % 2 == 0) ? Maybe.of(x) : Maybe.of((Integer)null);
                    }
                },
                Arrays.asList(Maybe.of(1), Maybe.of(2), Maybe.of((Integer)null), Maybe.of(4))
            )
        );
    }

    private static <T> void assertListsEqual(final List<T> l1, final List<T> l2) {
        assertEquals(l1.size(), l2.size());
        for (int i=0; i<l1.size(); ++i) {
            assertEquals(l1.get(i), l2.get(i));
        }
    }
}
