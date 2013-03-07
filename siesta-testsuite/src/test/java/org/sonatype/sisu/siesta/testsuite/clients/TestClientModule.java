package org.sonatype.sisu.siesta.testsuite.clients;

import static org.sonatype.sisu.siesta.client.ClientModule.clientModuleFor;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.inject.AbstractModule;

/**
 * TODO
 *
 * @since 1.0
 */
@Named
@Singleton
public class TestClientModule
    extends AbstractModule
{

    @Override
    protected void configure()
    {
        install( clientModuleFor( Users.class ) );
    }

}
