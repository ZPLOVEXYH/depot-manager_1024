package cn.samples.depot.common.config.netty;

/**
 * Description:
 *
 * @className: TCPServerStartFailedException
 * @Author: zhangpeng
 * @Date 2019/7/16 14:23
 * @Version 1.0
 **/
public class TCPServerStartFailedException extends RuntimeException {
    private static final long serialVersionUID = -3910184831521111428L;

    public TCPServerStartFailedException() {
    }

    public TCPServerStartFailedException(String message) {
        super(message);
    }

    public TCPServerStartFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TCPServerStartFailedException(Throwable cause) {
        super(cause);
    }

    public TCPServerStartFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
