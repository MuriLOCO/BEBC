package ca.concordia.BEBC.web;

import ca.concordia.BEBC.equations.EquationUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
public class EquationController {

    @RequestMapping(value = "/ncTwTrtot", method = RequestMethod.POST)
    public List<BigInteger> calculateNcTwTrtot(
            @RequestParam(required = true) BigInteger tr,
            @RequestParam(required = true) BigInteger tint){
        return EquationUtil.calculateNcTwTrtot(tr, tint);
    }

}
