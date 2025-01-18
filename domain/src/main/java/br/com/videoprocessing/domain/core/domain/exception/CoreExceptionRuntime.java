package br.com.videoprocessing.domain.core.domain.exception;

import java.io.Serializable;

public class CoreExceptionRuntime extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public CoreExceptionRuntime() {
        super();
    }
    public CoreExceptionRuntime(String msg)   {
        super(msg);
    }
    public CoreExceptionRuntime(String msg, Exception e)  {
        super(msg, e);
    }
}