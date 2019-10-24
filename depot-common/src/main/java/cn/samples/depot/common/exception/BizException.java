package cn.samples.depot.common.exception;

import lombok.extern.slf4j.Slf4j;


/**
 * 业务异常：因不符合业务规则导致的异常
 *
 * @author majunzi
 * @create 2018-07-04 9:13
 */
@Slf4j
public class BizException extends Exception {
    private static final long serialVersionUID = -3972788344776467724L;

    public BizException(String message) {
        super(message);
        log.error(message);
    }
}
