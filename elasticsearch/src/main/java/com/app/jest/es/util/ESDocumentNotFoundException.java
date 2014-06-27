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
public class ESDocumentNotFoundException extends  RuntimeException{
    public ESDocumentNotFoundException() {
        super();
    }

    public ESDocumentNotFoundException(String s) {
        super(s);
    }
}
