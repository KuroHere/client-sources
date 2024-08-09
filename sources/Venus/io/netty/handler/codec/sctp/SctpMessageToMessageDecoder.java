/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.handler.codec.sctp;

import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.sctp.SctpMessageCompletionHandler;

public abstract class SctpMessageToMessageDecoder
extends MessageToMessageDecoder<SctpMessage> {
    @Override
    public boolean acceptInboundMessage(Object object) throws Exception {
        if (object instanceof SctpMessage) {
            SctpMessage sctpMessage = (SctpMessage)object;
            if (sctpMessage.isComplete()) {
                return false;
            }
            throw new CodecException(String.format("Received SctpMessage is not complete, please add %s in the pipeline before this handler", SctpMessageCompletionHandler.class.getSimpleName()));
        }
        return true;
    }
}

