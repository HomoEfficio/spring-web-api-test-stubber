package homo.efficio.spring.web.api.test.stubber.restcontroller.annotated;

import org.springframework.web.bind.annotation.*;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016-11-23.
 */
@RestController
@RequestMapping({"/api/v1/members", "/api/v2/members"})
public class MemberController {

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public Member save(@RequestBody Member member) {
        return member;
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public Member findMemberBy(@PathVariable("userName") String userName) {
        return new Member(userName);
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.DELETE)
    public void deleteMember(@PathVariable("userName") String userName) {

    }

    class Member {

        private String userName;

        public Member(String userName) {
            this.userName = userName;
        }

        public String getUserName() {
            return userName;
        }
    }
}
