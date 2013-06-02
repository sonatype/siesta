/*
 * Copyright (c) 2007-2013 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.sisu.siesta.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Support for {@link Application} implementations.
 *
 * @since 1.0
 */
public class ApplicationSupport
    extends Application
{
    protected final Logger log;

    protected final Set<Class<?>> classes = Sets.newHashSet();

    protected final Set<Object> singletons = Sets.newHashSet();

    protected ApplicationSupport() {
        this.log = checkNotNull(createLogger());
    }

    protected Logger createLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return ImmutableSet.copyOf(classes);
    }

    @Override
    public Set<Object> getSingletons() {
        return ImmutableSet.copyOf(singletons);
    }
}
