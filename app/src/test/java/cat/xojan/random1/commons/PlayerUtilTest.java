package cat.xojan.random1.commons;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerUtilTest {

    @Test
    public void test_something() {
        String duration = PlayerUtil.millisToDuration(1000 * 60 * 60);

        assertEquals(duration, "01:00:00");
    }
}
