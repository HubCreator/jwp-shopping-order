package cart.ui;

import cart.application.ProductService;
import cart.repository.MemberRepository;
import cart.ui.dto.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ProductService productService;
    private final MemberRepository memberRepository;

    @GetMapping("/admin")
    public String admin(Model model) {
        final List<ProductResponse> productResponses = productService.getAllProducts().stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
        model.addAttribute("products", productResponses);
        return "admin";
    }

    @GetMapping("/settings")
    public String members(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "settings";
    }
}
