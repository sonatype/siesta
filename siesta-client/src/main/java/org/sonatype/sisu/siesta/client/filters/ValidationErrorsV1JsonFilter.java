package org.sonatype.sisu.siesta.client.filters;

import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_VALIDATION_ERRORS_V1_JSON_TYPE;

import javax.ws.rs.core.MediaType;

import org.sonatype.sisu.siesta.client.internal.ValidationErrorsV1FilterSupport;

/**
 * TODO
 *
 * @since 1.4
 */
public class ValidationErrorsV1JsonFilter
    extends ValidationErrorsV1FilterSupport
{

    protected MediaType getMediaType()
    {
        return VND_VALIDATION_ERRORS_V1_JSON_TYPE;
    }

}
