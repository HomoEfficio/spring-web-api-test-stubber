package homo.efficio.spring.web.api.test.stubber.restcontroller.annotated.nested;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016-11-23.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(value = "/abc", method = RequestMethod.POST)
    public ResponseEntity save() {
        return null;
    }

    @RequestMapping(value = "/abc/1")
    public ResponseEntity find() {
        return null;
    }
}
