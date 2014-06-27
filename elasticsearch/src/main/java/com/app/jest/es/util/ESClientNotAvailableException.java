package com.app.jest.es.util;

/**
 * Created with IntelliJ IDEA.
 *
 * @author yangq
 * @version 1.0
 *          <br>qing.yang@dewmobile.net</br>
 * @file TODO: file name
 * @date 14-6-25
 */
public class ESClientNotAvailableException extends RuntimeException {
    public ESClientNotAvailableException() {
        super();
    }

    /**
     *
     * @param s
     */
    public ESClientNotAvailableException(String s) {
        super(s);
    }
}
