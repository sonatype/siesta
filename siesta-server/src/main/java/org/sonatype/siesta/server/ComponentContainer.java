/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
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
