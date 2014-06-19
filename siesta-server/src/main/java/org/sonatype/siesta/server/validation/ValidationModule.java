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
package org.sonatype.siesta.server.validation;

import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import org.sonatype.siesta.Validate;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * {@link Module} that provides {@link Validator}s and enables validation of methods annotated with {@link Validate}.
 * 
 * @since 2.0
 */
public class ValidationModule
    extends AbstractModule
{
  @Override
  protected void configure() {
    final MethodInterceptor interceptor = new ValidationInterceptor();
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Validate.class), interceptor);
    requestInjection(interceptor);
  }

  @Provides
  @Singleton
  ValidatorFactory validatorFactory() {
    return Validation.buildDefaultValidatorFactory();
  }

  @Provides
  @Singleton
  Validator validator(final ValidatorFactory validatorFactory) {
    return validatorFactory.getValidator();
  }

  @Provides
  @Singleton
  ExecutableValidator executableValidator(final Validator validator) {
    return validator.forExecutables();
  }
}
