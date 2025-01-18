package br.com.videoprocessing.domain.core.domain.exception;

import java.io.Serializable;

public class CoreExceptionNegocial extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public CoreExceptionNegocial() {
        super();
    }
    public CoreExceptionNegocial(String msg)   {
        super(msg);
    }
    public CoreExceptionNegocial(String msg, Exception e)  {
        super(msg, e);
    }
}