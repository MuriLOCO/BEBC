package ca.concordia.BEBC.web;

import ca.concordia.BEBC.equations.EquationUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
public class EquationController {

    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public List<BigInteger> calculate(
            @RequestParam(required = true) BigInteger tr,
            @RequestParam(required = true) BigInteger tint,
            @RequestParam(required = true) BigInteger eavg,
            @RequestParam(required = true) BigInteger btc,
            @RequestParam(required = true) BigInteger bped,
            @RequestParam(required = true) BigInteger phvacavg,
            @RequestParam(required = true) BigInteger d,
            @RequestParam(required = true) BigInteger emax,
            @RequestParam(required = true) BigInteger ncgh,
            @RequestParam(required = true) BigInteger ds,
            @RequestParam(required = true) BigInteger bn,
            @RequestParam(required = true) BigInteger cv,
            @RequestParam(required = true) BigInteger cb,
            @RequestParam(required = true) BigInteger cc,
            @RequestParam(required = true) BigInteger ce,
            @RequestParam(required = true) BigInteger cdem,
            @RequestParam(required = true) BigInteger ts,
            @RequestParam(required = true) BigInteger drate,
            @RequestParam(required = true) BigInteger j){
        return EquationUtil.calculateAllValue(tr, tint, eavg, btc, bped, phvacavg, d, emax, ncgh, ds, bn, cv, cb, cc, ce, cdem, ts, drate, j);
    }
}
