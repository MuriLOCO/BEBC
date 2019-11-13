package ca.concordia.BEBC.equations;

import com.fasterxml.jackson.databind.node.BigIntegerNode;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class EquationUtil {

    private static BigInteger calculateNc(BigInteger tr, BigInteger tint) {
        return tr.divide(tint);
    }

    private static BigInteger calculateTw(BigInteger nc, BigInteger tr, BigInteger tint) {
        return nc.multiply(tint).subtract(tr);
    }

    private static BigInteger calculateTrtot(BigInteger tr, BigInteger tw) {
        return tr.add(tw);
    }

    private static BigInteger calculateEkmTot(BigInteger eavg, BigInteger btc, BigInteger bped, BigInteger phvacAvg, BigInteger tr, BigInteger d) {
        BigInteger firstPart = eavg.add(new BigInteger("0.1").multiply(btc.divide(bped)));
        BigInteger secondPart = phvacAvg.multiply(tr.divide(new BigInteger("3600").divide(d)));
        return firstPart.add(secondPart);
    }

    private static BigInteger calculatePchg(BigInteger emax, BigInteger d, BigInteger tw, BigInteger nchg) {
        return new BigInteger("3600").multiply(emax).multiply(d).divide(tw.multiply(nchg));
    }

    private static BigInteger calculateNbr(BigInteger ds, BigInteger bn, BigInteger btc, BigInteger ekmtot) {
        return ds.multiply(bn.multiply(btc).divide(ekmtot));
    }

    private static BigInteger calculateCBus(BigInteger nc, BigInteger cv, BigInteger cb, BigInteger cc, BigInteger ce, BigInteger ds, BigInteger ekmTot, BigInteger cdem, BigInteger nbr, BigInteger ts, BigInteger dRate, BigInteger j) {
        BigInteger summationValue = new BigInteger("0");
        //Resolve summation
        for (int i = 0; i < j.intValue(); i++) {
            BigInteger auxSummation = ce.multiply(ds.multiply(ekmTot)).add(cdem).add(cb.multiply(nbr.divide(ts)).multiply(new BigInteger("1").subtract(dRate).pow(-i)));
            summationValue = summationValue.add(auxSummation);
        }
        return nc.multiply(cv.add(cb)).add(cc).add(summationValue).divide(nc);
    }

    /**
     * Output all calculations in the order of
     * nc - number of buses
     * tw - wait time used for charging
     * tort - total trip time
     * ekmtot - total energy consumed on a predetermined route
     * pchg -  charging power
     * nbr - number of battery replacements during a bus’s service life
     * ncbus - total cost of ownership of an individual bus
     *
     * @param tr       - time spent driving
     * @param tint     - bus schedule interval
     * @param eavg     - average consumption (kWh/km)
     * @param btc      -battery capacity
     * @param bped     - battery pack energy density (Wh/kg)
     * @param phvacavg - average HVAC system (kW)
     * @param d        - distance travelled (round-trip)
     * @param emax     - maximum consumption (kWh/km)
     * @param ncgh     - charging efficiency
     * @param ds       - total distance travelled during service life
     * @param bn       - battery cycles
     * @param cv       - vehicle cost (k€)
     * @param cb       - battery cost (€/kWh)
     * @param cc       - charging station cost (€/kW)
     * @param ce       - electricity cost (€/kWh)
     * @param cdem     - demand cost ((€/kW)/y)
     * @param ts       - service life (y)
     * @param drate    - discounte rate (%)
     * @param j        - service years from first to last
     * @return
     */
    public static List<BigInteger> calculateAllValue(BigInteger tr, BigInteger tint, BigInteger eavg,
                                                     BigInteger btc, BigInteger bped, BigInteger phvacavg,
                                                     BigInteger d, BigInteger emax, BigInteger ncgh,
                                                     BigInteger ds, BigInteger bn, BigInteger cv,
                                                     BigInteger cb, BigInteger cc, BigInteger ce,
                                                     BigInteger cdem, BigInteger ts, BigInteger drate,
                                                     BigInteger j) {

        //Number of buses (nc)
        BigInteger nc = calculateNc(tr, tint);

        //Wait time used for charging
        BigInteger tw = calculateTw(nc, tr, tint);

        //Total trip time
        BigInteger tort = calculateTrtot(tr, tw);

        //Total energy consumed on a predetermined route
        BigInteger ekmtot = calculateEkmTot(eavg, btc, bped, phvacavg, tr, d);

        //Charging power
        BigInteger pchg = calculatePchg(emax, d, tw, ncgh);

        //Number of battery replacements during a bus’s service life
        BigInteger nbr = calculateNbr(ds, bn, btc, ekmtot);

        //Total cost of ownership of an individual bus
        BigInteger ncbus = calculateCBus(nc, cv, cb, cc, ce, ds, ekmtot, cdem, nbr, ts, drate, j);

        return Arrays.asList(nc, tw, tort, ekmtot, pchg, nbr, ncbus);
    }

}
