package com.course.api.spring_security_course.config.security.authorization;

import com.course.api.spring_security_course.exception.ObjectNotFoundException;
import com.course.api.spring_security_course.persistence.entity.User;
import com.course.api.spring_security_course.persistence.entity.security.GrantedPermissions;
import com.course.api.spring_security_course.persistence.entity.security.Operation;
import com.course.api.spring_security_course.persistence.repository.OperationRepository;
import com.course.api.spring_security_course.services.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private IUserService userService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {

        HttpServletRequest request = requestContext.getRequest();

        String url = extractUrl(request);

        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(url, httpMethod);
        if (isPublic) {
            return new AuthorizationDecision(true);
        }
        boolean isGranted = isGranted(url, httpMethod, authentication.get());

        return new AuthorizationDecision(isGranted);
    }

    private boolean isGranted(String url, String httpMethod, Authentication authentication) {
        if (authentication == null || !(authentication instanceof JwtAuthenticationToken)) {
            throw new AuthenticationCredentialsNotFoundException("User not logged in");
        }

//        if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
//            throw new AuthenticationCredentialsNotFoundException("User not logged in");
//        }
        List<Operation> operations = obtainedOperations(authentication);

        boolean isGranted = operations.stream().anyMatch(getOperationPredicate(url, httpMethod));

        return isGranted;
    }

    private static Predicate<Operation> getOperationPredicate(String url, String httpMethod) {
        return operation -> {
            String basePath = operation.getModule().getBasePath();
            Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
            Matcher matcher = pattern.matcher(url);
            return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
        };
    }

    private List<Operation> obtainedOperations(Authentication authentication) {
        JwtAuthenticationToken authToken = (JwtAuthenticationToken) authentication;
//        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication; casteo antiguo
        Jwt jwt = authToken.getToken();
//        String username = (String) authToken.getPrincipal();
        String username = jwt.getSubject();
        User user = userService.findOneByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User not found"));
//        return user.getRole().getPermissions().stream().map(GrantedPermissions::getOperation)
//                .collect(Collectors.toList());

        List<Operation> operations = user.getRole().getPermissions().stream()
                .map(grantedPermissions -> grantedPermissions.getOperation())
                .collect(Collectors.toList());

        List<String> scopes = extractScopes(jwt);

        if (!scopes.contains("ALL")) {
            operations = operations.stream().filter(operation -> scopes.contains(operation.getName())).collect(Collectors.toList()); //valido que las operaciones esten en mi lista de scopes
        }
        return operations;
    }

    private List<String> extractScopes(Jwt jwt) {
        List<String> scopes = new ArrayList<>();
        try {
            scopes = (List<String>) jwt.getClaims().get("scope");
        } catch (Exception e) {
            System.out.println(e);
        }
        return scopes;
    }

    private boolean isPublic(String url, String httpMethod) {
        List<Operation> publicAccessEndpoints = operationRepository.findByPublicAccess();
        boolean isPublic = publicAccessEndpoints.stream().anyMatch(getOperationPredicate(url, httpMethod));
        return isPublic;
    }

    private String extractUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath(); // /api/v1
        String url = request.getRequestURI();
        url = url.replace(contextPath, ""); //quito el context path
        return url;
    }
}
