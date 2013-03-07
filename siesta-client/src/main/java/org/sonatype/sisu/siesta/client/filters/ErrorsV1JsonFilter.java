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
package org.sonatype.sisu.siesta.client.filters;

import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_ERROR_V1_JSON_TYPE;

import javax.ws.rs.core.MediaType;

import org.sonatype.sisu.siesta.client.internal.ErrorsV1FilterSupport;

/**
 * TODO
 *
 * @since 1.4
 */
public class ErrorsV1JsonFilter
    extends ErrorsV1FilterSupport
{

    protected MediaType getMediaType()
    {
        return VND_ERROR_V1_JSON_TYPE;
    }

}
