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
package org.sonatype.sisu.siesta.server.internal.mappers;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.sisu.siesta.common.validation.ValidationErrorXO;
import org.sonatype.sisu.siesta.common.validation.ValidationErrorsException;
import org.sonatype.sisu.siesta.server.ValidationErrorsExceptionMappersSupport;

/**
 * Maps {@link ValidationErrorsException} to 400 with a list of {@link ValidationErrorXO} as body.
 *
 * @since 1.4
 */
@Named
@Singleton
public class ValidationErrorsExceptionMapper
    extends ValidationErrorsExceptionMappersSupport<ValidationErrorsException>
{

  @Override
  protected List<ValidationErrorXO> getValidationErrors(final ValidationErrorsException exception) {
    return exception.getValidationErrors();
  }

}