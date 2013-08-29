/*
 * Copyright (c) 2007-2013 Sonatype, Inc. All rights reserved.
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

import java.util.Arrays;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.Filterable;

/**
 * A filter that allows specifying filters per request.
 *
 * @since 1.5.1
 */
public class RequestFilters
    extends ClientFilter
{

  public static final String KEY = RequestFilters.class.getName();

  @Override
  public ClientResponse handle(final ClientRequest request) throws ClientHandlerException {
    final Object requestFiltersProperty = request.getProperties().get(KEY);
    if (isClientFilterArray(requestFiltersProperty)) {
      final Filterable filterable = new Filterable(getNext()) {};
      for (ClientFilter clientFilter : Lists.reverse(Arrays.asList((ClientFilter[]) requestFiltersProperty))) {
        filterable.addFilter(clientFilter);
      }
      return filterable.getHeadHandler().handle(request);
    }
    return getNext().handle(request);
  }

  public static void addRequestFilters(final Map<String, Object> properties, final ClientFilter... filters) {
    final Object requestFiltersProperty = properties.get(KEY);
    properties.put(
        KEY,
        isClientFilterArray(requestFiltersProperty)
            ? ObjectArrays.concat((ClientFilter[]) requestFiltersProperty, filters, ClientFilter.class)
            : filters
    );
  }

  private static boolean isClientFilterArray(final Object requestFiltersProperty) {
    if (requestFiltersProperty == null) {
      return false;
    }
    try {
      final ClientFilter[] casted = (ClientFilter[]) requestFiltersProperty;
      return true;
    }
    catch (ClassCastException ignore) {
      return false;
    }
  }

}
