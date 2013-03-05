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
package org.sonatype.sisu.siesta.common.exceptions;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @since 1.4
 */
@XmlRootElement( name = "error" )
public class ErrorXO
{

    private String uuid;

    private String message;

    public ErrorXO()
    {
    }

    public ErrorXO( final String uuid, final String message )
    {
        this.uuid = checkNotNull( uuid );
        this.message = message;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid( final String uuid )
    {
        this.uuid = uuid;
    }

    public ErrorXO withId( final String id )
    {
        this.uuid = id;
        return this;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( final String message )
    {
        this.message = message;
    }

    public ErrorXO withMessage( final String message )
    {
        this.message = message;
        return this;
    }

    @Override
    public String toString()
    {
        return message + " (UUID " + uuid + ")";
    }

}