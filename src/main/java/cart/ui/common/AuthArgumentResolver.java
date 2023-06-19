package cart.ui.common;

import cart.domain.auth.Auth;
import cart.domain.member.MemberEmail;
import cart.domain.member.MemberPassword;
import cart.exception.authentication.InvalidFormatException;
import cart.exception.authentication.PasswordNotMatchException;
import cart.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthRepository authRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Auth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            return null;
        }

        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            return null;
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");

        if (credentials.length != 2) {
            throw new InvalidFormatException();
        }
        String email = credentials[0];
        String password = credentials[1];

        // 본인 여부 확인
        Auth auth = authRepository.findByEmail(new MemberEmail(email));
        if (!auth.checkPassword(new MemberPassword(password))) {
            throw new PasswordNotMatchException();
        }
        return auth;
    }
}
