/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.bookkeeper.proto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * SlicerHandler calls `.slice()` on the ByteBuf and writes the sliced ByteBuf to the channel.
 * The intention is to prevent SslHandler from mutating the input ByteBuf.
 * By default, SslHandler will attempt to use the input ByteBuf as a coalescing buffer
 * and mutate the input ByteBuf. This can be avoided by slicing the input ByteBuf since a sliced
 * buffer doesn't contain unallocated space.
 */
@Sharable
class SlicerHandler extends ChannelOutboundHandlerAdapter {
    static final String NAME = "slicer";
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf) {
            ctx.write(((ByteBuf) msg).slice(), promise);
        } else {
            ctx.write(msg, promise);
        }
    }
}