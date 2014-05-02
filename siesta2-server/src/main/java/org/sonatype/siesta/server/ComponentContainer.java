package org.sonatype.siesta.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sonatype.siesta.Component;
import org.sonatype.siesta.Resource;

import org.eclipse.sisu.BeanEntry;

/**
 * Siesta {@link Component} (and {@link Resource} container abstraction.
 *
 * @since 2.0
 */
public interface ComponentContainer
{
  void init(final ServletConfig config) throws ServletException;

  void service(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

  void destroy();

  void addComponent(BeanEntry<?,?> entry) throws Exception;

  void removeComponent(BeanEntry<?,?> entry) throws Exception;
}
