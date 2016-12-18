package cat.xojan.random1.commons;

import org.junit.Test;

public class ErrorUtilTest {

    @Test
    public void should_log_exception() {
        ErrorUtil.logException(new NullPointerException());
    }

    @Test
    public void should_log_exception_message() {
        ErrorUtil.logException("exception");
    }
}
