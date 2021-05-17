/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.utils;

import javax.servlet.http.HttpServletRequest;
import org.dspace.core.Context;
import org.dspace.core.Context;
import org.dspace.xoai.services.api.context.ContextService;
import org.dspace.xoai.services.api.context.ContextServiceException;
import org.dspace.xoai.services.impl.context.DSpaceContextService;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Primary
public class MockContextImpl implements ContextService {

  private static final String OAI_CONTEXT = "OAI_CONTEXT";

  @Override
  public Context getContext() throws ContextServiceException {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    Object value = request.getAttribute(OAI_CONTEXT);
    if (value == null || !(value instanceof Context)) {
      request.setAttribute(OAI_CONTEXT, null);
    }
    return (Context) request.getAttribute(OAI_CONTEXT);
  }

}