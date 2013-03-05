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
package org.sonatype.sisu.siesta.common;

import javax.ws.rs.core.MediaType;

/**
 * Siesta specific media types.
 *
 * @since 1.4
 */
public interface SiestaMediaType
{

    public static final String VND_NEXUS_ERROR_V1_XML =
        "application/vnd.nexus-error-v1+xml";

    public static final MediaType VND_NEXUS_ERROR_V1_XML_TYPE =
        new MediaType( "application", "vnd.nexus-error-v1+xml" );

    public static final String VND_NEXUS_ERROR_V1_JSON =
        "application/vnd.nexus-error-v1+json";

    public static final MediaType VND_NEXUS_ERROR_V1_JSON_TYPE =
        new MediaType( "application", "vnd.nexus-error-v1+json" );

    public static final String VND_NEXUS_VALIDATION_ERRORS_V1_XML =
        "application/vnd.nexus-validation-errors-v1+xml";

    public static final MediaType VND_NEXUS_VALIDATION_ERRORS_V1_XML_TYPE =
        new MediaType( "application", "vnd.nexus-validation-errors-v1+xml" );

    public static final String VND_NEXUS_VALIDATION_ERRORS_V1_JSON =
        "application/vnd.nexus-validation-errors-v1+json";

    public static final MediaType VND_NEXUS_VALIDATION_ERRORS_V1_JSON_TYPE =
        new MediaType( "application", "vnd.nexus-validation-errors-v1+json" );

}
