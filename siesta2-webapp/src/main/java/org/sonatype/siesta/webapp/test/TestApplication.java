package org.sonatype.siesta.webapp.test;

import java.util.Set;

import javax.ws.rs.core.Application;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test application.
 *
 * @since 2.0
 */
public class TestApplication
   extends Application
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final Set<Class<?>> classes = Sets.newHashSet();

  public TestApplication() {
    if (log.isTraceEnabled()) {
      log.trace("Created", new Throwable("MARKER"));
    }
    else {
      log.debug("Created");
    }

    classes.add(TestResource.class);
  }

  @Override
  public Set<Class<?>> getClasses() {
    return ImmutableSet.copyOf(classes);
  }
}
