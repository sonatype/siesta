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
package org.sonatype.sisu.siesta.testsuite.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.sonatype.sisu.siesta.common.Resource;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @since 1.4.2
 */
@Named
@Singleton
@Path( "/echo" )
public class EchoResource
    implements Resource
{

    @GET
    @Produces( { APPLICATION_XML, APPLICATION_JSON } )
    public List<String> get( @QueryParam( "foo" ) String foo,
                             @QueryParam( "bar" ) Integer bar )
    {
        final List<String> result = Lists.newArrayList();
        if ( foo != null )
        {
            result.add( "foo=" + foo );
        }
        if ( bar != null )
        {
            result.add( "bar=" + bar );
        }
        return result;
    }

    @GET
    @Path( "/multiple" )
    @Produces( { APPLICATION_XML, APPLICATION_JSON } )
    public List<String> get( @QueryParam( "foo" ) List<String> foo )
    {
        return Lists.transform( foo, new Function<String, String>()
        {
            @Nullable
            public String apply( @Nullable final String value )
            {
                return "foo=" + value;
            }
        } );
    }

}
