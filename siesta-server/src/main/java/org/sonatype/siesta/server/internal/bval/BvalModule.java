/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
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
package org.sonatype.siesta.server.internal.bval;

import javax.validation.ValidationProviderResolver;
import javax.validation.spi.BootstrapState;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.bval.guice.Validate;
import org.apache.bval.guice.ValidationModule;

/**
 * <a href="https://bval.apache.org">Apache BVal</a> Guice module.
 *
 * Allows {@link Validate} to be used to apply validation around a method invocation.
 *
 * Additional provides some workarounds for {@link ValidationModule} quirks.
 *
 * @since 2.0
 */
public class BvalModule
    extends AbstractModule
{
  @Override
  protected void configure() {
    // BootstrapState should not be bounded as it is marked as optional in ConfigurationStateProvider
    bind(BootstrapState.class).toInstance(new BootstrapState()
    {
      @Override
      public ValidationProviderResolver getValidationProviderResolver() {
        return null;
      }

      @Override
      public ValidationProviderResolver getDefaultValidationProviderResolver() {
        return null;
      }
    });

    // TCCL workaround for Apache/BVal visibility issue
    binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(Validate.class), new MethodInterceptor()
    {
      public Object invoke(final MethodInvocation mi) throws Throwable {
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        try {
          return mi.proceed();
        }
        finally {
          Thread.currentThread().setContextClassLoader(cl);
        }
      }
    });

    install(new ValidationModule());
  }
}