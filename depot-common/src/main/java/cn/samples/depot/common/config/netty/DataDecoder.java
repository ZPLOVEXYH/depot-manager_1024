package cn.samples.depot.common.config.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 *
 * @className: DataDecoder
 * @Author: zhangpeng
 * @Date 2019/7/16 14:04
 * @Version 1.0
 **/
@Component
@Scope("prototype")
public class DataDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

    }

}
