package cart.ui;

import cart.application.MemberService;
import cart.domain.auth.Auth;
import cart.domain.member.MemberPoint;
import cart.ui.dto.order.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/point")
    public ResponseEntity<PointResponse> getPoint(final Auth auth) {
        final MemberPoint point = memberService.getPoint(auth);
        return ResponseEntity.ok(new PointResponse(point.getPoint()));
    }
}
