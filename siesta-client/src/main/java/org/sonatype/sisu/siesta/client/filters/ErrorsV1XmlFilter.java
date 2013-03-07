package org.sonatype.sisu.siesta.client.filters;

import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_ERROR_V1_XML_TYPE;

import javax.ws.rs.core.MediaType;

import org.sonatype.sisu.siesta.client.internal.ErrorsV1FilterSupport;

/**
 * TODO
 *
 * @since 1.4
 */
public class ErrorsV1XmlFilter
    extends ErrorsV1FilterSupport
{

    protected MediaType getMediaType()
    {
        return VND_ERROR_V1_XML_TYPE;
    }

}
