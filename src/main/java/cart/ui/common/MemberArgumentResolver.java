package cart.ui.common;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.domain.member.MemberPassword;
import cart.exception.authentication.InvalidFormatException;
import cart.exception.authentication.PasswordNotMatchException;
import cart.exception.notfound.MemberNotFoundException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberDao memberDao;

    public MemberArgumentResolver(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
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
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));
        if (!member.checkPassword(new MemberPassword(password))) {
            throw new PasswordNotMatchException();
        }
        return member;
    }
}
