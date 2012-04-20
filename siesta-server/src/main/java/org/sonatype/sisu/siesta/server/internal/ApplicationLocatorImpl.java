/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
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
package org.sonatype.sisu.siesta.server.internal;

import com.google.common.collect.ImmutableList;
import org.sonatype.sisu.siesta.server.ApplicationLocator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Application;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link ApplicationLocator} implementation.
 *
 * @since 1.0
 */
@Named
public class ApplicationLocatorImpl
    implements ApplicationLocator
{
    private final List<Application> applications;

    @Inject
    public ApplicationLocatorImpl(final List<Application> applications) {
        this.applications = checkNotNull(applications);
    }

    public Collection<Application> locate() {
        return ImmutableList.copyOf(applications);
    }
}
