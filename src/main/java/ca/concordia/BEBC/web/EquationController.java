package ca.concordia.BEBC.web;

import ca.concordia.BEBC.equations.EquationUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class EquationController {

    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public List<BigDecimal> calculate(
            @RequestParam(required = true) String tr,
            @RequestParam(required = true) String tint,
            @RequestParam(required = true) String eavg,
            @RequestParam(required = true) String btc,
            @RequestParam(required = true) String bped,
            @RequestParam(required = true) String phvacavg,
            @RequestParam(required = true) String d,
            @RequestParam(required = true) String emax,
            @RequestParam(required = true) String ncgh,
            @RequestParam(required = true) String ds,
            @RequestParam(required = true) String bn,
            @RequestParam(required = true) String cv,
            @RequestParam(required = true) String cb,
            @RequestParam(required = true) String cc,
            @RequestParam(required = true) String ce,
            @RequestParam(required = true) String cdem,
            @RequestParam(required = true) String ts,
            @RequestParam(required = true) String drate,
            @RequestParam(required = true) String j){
        return EquationUtil.calculateAllValue(tr, tint, eavg, btc, bped, phvacavg, d, emax, ncgh, ds, bn, cv, cb, cc, ce, cdem, ts, drate, j);
    }
}
