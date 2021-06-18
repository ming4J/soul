/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.soul.plugin.ratelimiter.handler;

import java.util.List;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.ReactiveScriptExecutor;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisElementReader;
import org.springframework.data.redis.serializer.RedisElementWriter;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import reactor.core.publisher.Flux;

/**
 * The type reactive redisTemplate.
 *
 * @author zhanglei
 */
public class SoulReactiveRedisTemplate<K, V> extends ReactiveRedisTemplate<K, V> {

    private final ReactiveScriptExecutor<K> reactiveScriptExecutor;

    public SoulReactiveRedisTemplate(final ReactiveRedisConnectionFactory connectionFactory, final RedisSerializationContext<K, V> serializationContext) {
        super(connectionFactory, serializationContext);
        this.reactiveScriptExecutor = new SoulReactiveScriptExecutor<>(connectionFactory, serializationContext);
    }

    @Override
    public <T> Flux<T> execute(final RedisScript<T> script, final List<K> keys, final List<?> args) {
        return reactiveScriptExecutor.execute(script, keys, args);
    }

    @Override
    public <T> Flux<T> execute(final RedisScript<T> script, final List<K> keys, final List<?> args, final RedisElementWriter<?> argsWriter,
                               final RedisElementReader<T> resultReader) {
        return reactiveScriptExecutor.execute(script, keys, args, argsWriter, resultReader);
    }
}
