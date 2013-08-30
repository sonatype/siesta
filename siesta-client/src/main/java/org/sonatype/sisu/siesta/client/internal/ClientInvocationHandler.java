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

package org.sonatype.sisu.siesta.client.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.sonatype.sisu.siesta.client.Filters;
import org.sonatype.sisu.siesta.client.filters.RequestFilters;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * TODO
 *
 * @since 1.4
 */
public class ClientInvocationHandler
    implements InvocationHandler
{

  private final Class<?> serviceInterface;

  private final Client client;

  private final String baseUrl;

  public ClientInvocationHandler(final Class<?> serviceInterface, final Client client, final String baseUrl) {
    this.serviceInterface = serviceInterface;
    this.client = client;
    try {
      if (baseUrl.endsWith("/")) {
        this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
      }
      else {
        this.baseUrl = baseUrl;
      }
    }
    catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public Object invoke(final Object proxy, final Method method, final Object[] args)
      throws Throwable
  {
    final String url = getUrl(method, args);
    final MultivaluedMap<String, String> queryParams = getQueryParams(method, args);
    final String httpMethod = getHttpMethod(method);
    final String type = getType(method);
    final String[] accepts = getAccepts(method);
    final Object payload = getPayload(method, args);

    WebResource resource = client.resource(url);

    // TODO determine filters from annotations
    final ClientFilter[] filters = getFilters(method);
    if (filters != null) {
      RequestFilters.addRequestFilters(resource.getProperties(), filters);
    }

    if (queryParams != null && !queryParams.isEmpty()) {
      resource = resource.queryParams(queryParams);
    }

    final WebResource.Builder builder = resource.accept(accepts);

    if (type != null) {
      builder.type(type);
    }

    ClientResponse response;
    if (payload == null) {
      response = builder.method(httpMethod, ClientResponse.class);
    }
    else {
      response = builder.method(httpMethod, ClientResponse.class, payload);
    }

    final Class<?> returnType = method.getReturnType();

    if (ClientResponse.class.equals(returnType)) {
      return response;
    }

    if (Response.Status.Family.SUCCESSFUL.equals(response.getClientResponseStatus().getFamily())) {
      if (!void.class.equals(returnType)) {
        return response.getEntity(new GenericType(method.getGenericReturnType()));
      }
      else {
        return null;
      }
    }

    throw new UniformInterfaceException(response);
  }

  private MultivaluedMap<String, String> getQueryParams(final Method method, final Object[] args) {
    if (args != null) {
      final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();

      final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

      for (int i = 0; i < args.length; i++) {
        if (args[i] != null) {
          final Annotation[] annotations = parameterAnnotations[i];
          if (annotations.length > 0) {
            for (final Annotation annotation : annotations) {
              if (annotation instanceof QueryParam) {
                if (args[i] instanceof MultivaluedMap) {
                  final MultivaluedMap map = (MultivaluedMap) args[i];
                  final Set keySet = map.keySet();
                  for (Object key : keySet) {
                    for (Object value : (List) map.get(key)) {
                      if (value != null) {
                        queryParams.add(key.toString(), value.toString());
                      }
                      else {
                        queryParams.add(key.toString(), null);
                      }
                    }
                  }
                }
                else if (args[i].getClass().isArray()) {
                  for (final Object entry : (Object[]) args[i]) {
                    if (entry != null) {
                      queryParams.add(((QueryParam) annotation).value(), entry.toString());
                    }
                  }
                }
                else if (args[i] instanceof Iterable) {
                  for (final Object entry : (Iterable) args[i]) {
                    if (entry != null) {
                      queryParams.add(((QueryParam) annotation).value(), entry.toString());
                    }
                  }
                }
                else if (args[i] instanceof Iterator) {
                  final Iterator it = (Iterator) args[i];
                  while (it.hasNext()) {
                    final Object entry = it.next();
                    if (entry != null) {
                      queryParams.add(((QueryParam) annotation).value(), entry.toString());
                    }
                  }
                }
                else {
                  queryParams.add(((QueryParam) annotation).value(), args[i].toString());
                }
              }
            }
          }
        }
      }
      if (!queryParams.isEmpty()) {
        return queryParams;
      }
    }
    return null;
  }

  private Object getPayload(final Method method, final Object[] args) {
    if (args != null) {
      final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

      for (int i = 0; i < args.length; i++) {
        final Annotation[] annotations = parameterAnnotations[i];
        if (annotations.length > 0) {
          for (final Annotation annotation : annotations) {
            if (!(annotation instanceof PathParam) && !(annotation instanceof QueryParam)) {
              return args[i];
            }
          }
        }
        else {
          return args[i];
        }
      }
    }
    return null;
  }

  private String[] getAccepts(final Method method) {
    final Consumes consumes = method.getAnnotation(Consumes.class);
    if (consumes != null) {
      return consumes.value();
    }
    return new String[]{MediaType.APPLICATION_JSON};
  }

  private String getType(final Method method) {
    final Produces produces = method.getAnnotation(Produces.class);
    if (produces != null) {
      return produces.value()[0];
    }
    return MediaType.APPLICATION_JSON;
  }

  private String getHttpMethod(final Method method) {
    final Annotation[] annotations = method.getAnnotations();
    if (annotations != null) {
      for (final Annotation annotation : annotations) {
        final HttpMethod httpMethod = annotation.annotationType().getAnnotation(HttpMethod.class);
        if (httpMethod != null) {
          return httpMethod.value();
        }
      }
    }
    throw new IllegalStateException("Method " + method + " is not annotated with any of @GET/...");
  }

  private String getUrl(final Method method, final Object[] args) {
    final StringBuilder rawUrl = new StringBuilder();
    final Path atClass = serviceInterface.getAnnotation(Path.class);
    if (atClass != null) {
      rawUrl.append(atClass.value());
    }
    else {
      final Path atDeclaringClass = method.getDeclaringClass().getAnnotation(Path.class);
      if (atDeclaringClass != null) {
        rawUrl.append(atDeclaringClass.value());
      }
    }
    final Path atMethod = method.getAnnotation(Path.class);
    if (atMethod != null) {
      rawUrl.append(atMethod.value());
    }
    if (rawUrl.toString().trim().length() == 0) {
      throw new IllegalStateException(String.format(
          "Cannot calculate rest URL for [%s]. Is class and/or method annotated with @Path?",
          method.getName())
      );
    }

    String url = rawUrl.toString();
    if (url.contains("{") && url.contains("}")) {
      final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
      for (int i = 0; i < parameterAnnotations.length; i++) {
        if (args.length >= i) {
          final Annotation[] annotations = parameterAnnotations[i];
          for (final Annotation annotation : annotations) {
            if (annotation instanceof PathParam) {
              final String name = ((PathParam) annotation).value();
              if (args[i] != null) {
                url = url.replace("{" + name + "}", args[i].toString());
              }
            }
          }
        }
      }
    }
    return baseUrl + url;
  }

  private ClientFilter[] getFilters(final Method method) {
    {
      final List<ClientFilter> filters = getClientFilters(method.getAnnotation(Filters.class));
      if (!filters.isEmpty()) {
        return filters.toArray(new ClientFilter[filters.size()]);
      }
    }
    {
      final List<ClientFilter> filters = getClientFilters(method.getDeclaringClass());
      if (!filters.isEmpty()) {
        return filters.toArray(new ClientFilter[filters.size()]);
      }
    }
    return null;
  }

  private List<ClientFilter> getClientFilters(final Class<?> clazz) {
    {
      final List<ClientFilter> filters = getClientFilters(clazz.getAnnotation(Filters.class));
      if (!filters.isEmpty()) {
        return filters;
      }
    }
    for (Class<?> extended : clazz.getInterfaces()) {
      final List<ClientFilter> filters = getClientFilters(extended);
      if (!filters.isEmpty()) {
        return filters;
      }
    }
    return Lists.newArrayList();
  }

  private List<ClientFilter> getClientFilters(final Filters annotation) {
    final List<ClientFilter> filters = Lists.newArrayList();
    if (annotation != null) {
      for (final Class<? extends ClientFilter> filterClass : annotation.value()) {
        try {
          filters.add(filterClass.newInstance());
        }
        catch (Exception e) {
          throw Throwables.propagate(e);
        }
      }
    }
    return filters;
  }

}
