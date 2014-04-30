package org.sonatype.siesta.server.internal;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;

import com.google.inject.Injector;
import com.google.inject.Key;
import org.eclipse.sisu.BeanEntry;
import org.eclipse.sisu.inject.BeanLocator;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.server.spi.ComponentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkState;

/**
 * Sisu {@link ComponentProvider}.
 *
 * @since 2.0
 */
public class SisuComponentProvider
    implements ComponentProvider
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  private ServiceLocator serviceLocator;

  private BeanLocator beanLocator;

  @Override
  public void initialize(final ServiceLocator locator) {
    log.debug("Service locator: {}", locator);
    this.serviceLocator = locator;

    ServletContext servletContext = locator.getService(ServletContext.class);
    Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());
    log.trace("Injector: {}", injector);

    this.beanLocator = injector.getInstance(BeanLocator.class);
    log.debug("Bean locator: {}", beanLocator);
  }

  @Override
  public void done() {
    log.debug("Done");
  }

  @Override
  public boolean bind(final Class<?> type, final Set<Class<?>> providerContracts) {
    log.trace("Bind: {}, contracts: {}", type, providerContracts);

    checkState(beanLocator != null);

    BeanEntry entry = lookup(type);
    if (entry == null) {
      return false;
    }

    DynamicConfiguration config = Injections.getConfiguration(serviceLocator);
    ServiceBindingBuilder binding = Injections.newFactoryBinder(new SisuComponentFactory(entry, serviceLocator));
    binding.to(type);
    Injections.addBinding(binding, config);
    config.commit();

    log.debug("Registered: {} with key: {}", entry.getImplementationClass(), entry.getKey());
    return true;
  }

  @SuppressWarnings("unchecked")
  private BeanEntry lookup(final Class<?> type) {
    log.trace("Lookup: {}", type);
    Iterator<BeanEntry<Annotation, ?>> iter = beanLocator.locate(Key.get((Class) type)).iterator();
    if (iter.hasNext()) {
      return iter.next();
    }
    return null;
  }

  private static class SisuComponentFactory
    implements Factory
  {
    private final BeanEntry entry;

    private final ServiceLocator locator;

    private SisuComponentFactory(final BeanEntry entry, final ServiceLocator locator) {
      this.entry = entry;
      this.locator = locator;
    }

    @Override
    public Object provide() {
      Object value = entry.getValue();
      locator.inject(value);
      return value;
    }

    @Override
    public void dispose(final Object instance) {
      // nothing
    }
  }
}
