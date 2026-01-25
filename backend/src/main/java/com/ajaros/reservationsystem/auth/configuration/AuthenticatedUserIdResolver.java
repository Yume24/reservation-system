package com.ajaros.reservationsystem.auth.configuration;

import com.ajaros.reservationsystem.auth.annotations.AuthenticatedUserId;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticatedUserIdResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(AuthenticatedUserId.class)
        && (parameter.getParameterType().equals(Long.class)
            || parameter.getParameterType().equals(long.class));
  }

  @Override
  public Object resolveArgument(
          @NonNull MethodParameter parameter,
          ModelAndViewContainer mavContainer,
          @NonNull NativeWebRequest webRequest,
          WebDataBinderFactory binderFactory) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
      return jwt.getClaims().get("id");
    }
    return null;
  }
}
