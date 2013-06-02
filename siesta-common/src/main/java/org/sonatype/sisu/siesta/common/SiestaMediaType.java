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
package org.sonatype.sisu.siesta.common;

import javax.ws.rs.core.MediaType;

/**
 * Siesta specific media types.
 *
 * @since 1.4
 */
public interface SiestaMediaType
{

    String VND_ERROR_V1_XML = "application/vnd.siesta-error-v1+xml";

    MediaType VND_ERROR_V1_XML_TYPE = new MediaType( "application", "vnd.siesta-error-v1+xml" );

    String VND_ERROR_V1_JSON = "application/vnd.siesta-error-v1+json";

    MediaType VND_ERROR_V1_JSON_TYPE = new MediaType( "application", "vnd.siesta-error-v1+json" );

    String VND_VALIDATION_ERRORS_V1_XML = "application/vnd.siesta-validation-errors-v1+xml";

    MediaType VND_VALIDATION_ERRORS_V1_XML_TYPE = new MediaType( "application", "vnd.siesta-validation-errors-v1+xml" );

    String VND_VALIDATION_ERRORS_V1_JSON = "application/vnd.siesta-validation-errors-v1+json";

    MediaType VND_VALIDATION_ERRORS_V1_JSON_TYPE = new MediaType( "application", "vnd.siesta-validation-errors-v1+json" );

}
