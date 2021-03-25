package com.test.tworldapplication;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testCheckMobile() {
        System.out.printf("result:" + checkPhone("162275300061"));
    }

    @Test
    public void testAtomicInteger() {
        AtomicInteger atom = new AtomicInteger(0);
        atom.incrementAndGet();
        atom.incrementAndGet();
        atom.incrementAndGet();
        atom.incrementAndGet();
        System.out.println(atom.get() + "");
        atom.set(0);
        System.out.println("reset:" + atom.get());
        atom.incrementAndGet();
        atom.incrementAndGet();
        atom.incrementAndGet();
        atom.incrementAndGet();
        System.out.println("reset:" + (atom.get() == 4));
    }

    protected boolean checkPhone(String str) {
        String pattern = "0?[0-9]{11}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }

}