package homo.efficio.spring.web.api.test.stubber.restcontroller.annotated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016-11-23.
 */
@RestController
@RequestMapping({"/api/v1/members", "/api/v2/members"})
public class MemberController {
}
