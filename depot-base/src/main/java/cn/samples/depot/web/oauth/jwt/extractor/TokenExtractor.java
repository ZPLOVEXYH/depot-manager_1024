package cn.samples.depot.web.oauth.jwt.extractor;

/**
 * Description:
 *
 * @className: TokenExtractor
 * @Author: zhangpeng
 * @Date 2019/7/16 13:57
 * @Version 1.0
 **/
public interface TokenExtractor {
    String extract(String payload);
}
