package ca.concordia.BEBC.equations;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class EquationUtil {

    /**
     *
     * @param tr -  time spent driving
     * @param tint - bus schedule interval
     * @return
     */
    private static BigDecimal calculateNc(BigDecimal tr, BigDecimal tint) {
        return tr.divide(tint, 2, RoundingMode.HALF_EVEN);
    }

    /**
     *
     * @param nc - number of buses
     * @param tr -  time spent driving
     * @param tint - bus schedule interval
     * @return
     */
    private static BigDecimal calculateTw(BigDecimal nc, BigDecimal tr, BigDecimal tint) {
        return nc.multiply(tint).subtract(tr);
    }

    /**
     *
     * @param tr - time spent driving
     * @param tw - wait time used for charging
     * @return
     */
    private static BigDecimal calculateTrtot(BigDecimal tr, BigDecimal tw) {
        return tr.add(tw);
    }

    /**
     *
     * @param eavg - average consumption (kWh/km)
     * @param btc      - battery capacity
     * @param bped     - battery pack energy density (Wh/kg)
     * @param phvacavg - average HVAC system (kW)
     * @param tr       - time spent driving
     * @param d        - distance travelled (round-trip)
     * @return
     */
    private static BigDecimal calculateEkmTot(BigDecimal eavg, BigDecimal btc, BigDecimal bped, BigDecimal phvacavg, BigDecimal tr, BigDecimal d) {
        BigDecimal firstPart = eavg.add(new BigDecimal("0.1").multiply(btc).divide(bped, 2, RoundingMode.HALF_EVEN));
        BigDecimal secondPart = phvacavg.multiply(tr).divide(new BigDecimal("3600").divide(d, 2, RoundingMode.HALF_EVEN), 2, RoundingMode.HALF_EVEN);
        return firstPart.add(secondPart);
    }

    /**
     *
     * @param emax - maximum consumption (kWh/km)
     * @param d - distance travelled (round-trip)
     * @param tw - wait time used for charging
     * @param nchg - charging efficiency
     * @return
     */
    private static BigDecimal calculatePchg(BigDecimal emax, BigDecimal d, BigDecimal tw, BigDecimal nchg) {
        return new BigDecimal("3600").multiply(emax).multiply(d).divide(tw.multiply(nchg), 2, RoundingMode.HALF_EVEN);
    }

    /**
     *
     * @param ds - total distance travelled during service life
     * @param bn - battery cycles
     * @param btc - battery capacity
     * @param ekmtot - total energy consumed on a predetermined route
     * @return
     */
    private static BigDecimal calculateNbr(BigDecimal ds, BigDecimal bn, BigDecimal btc, BigDecimal ekmtot) {
        return ds.multiply(bn.multiply(btc).divide(ekmtot, 2, RoundingMode.HALF_EVEN));
    }

    /**
     *
     * @param nc - number of buses
     * @param cv - vehicle cost (k€)
     * @param cb - battery cost (€/kWh)
     * @param cc - charging station cost (€/kW)
     * @param ce - electricity cost (€/kWh)
     * @param ds - total distance travelled during service life
     * @param ekmtot - total energy consumed on a predetermined route
     * @param cdem - demand cost ((€/kW)/y)
     * @param nbr - number of battery replacements during a bus’s service life
     * @param ts - service life (y)
     * @param drate - discounte rate (%)
     * @param j - service years from first to last
     * @return
     */
    private static BigDecimal calculateCBus(BigDecimal nc, BigDecimal cv, BigDecimal cb, BigDecimal cc, BigDecimal ce, BigDecimal ds, BigDecimal ekmtot, BigDecimal cdem, BigDecimal nbr, BigDecimal ts, BigDecimal drate, BigDecimal j) {
        BigDecimal summationValue = new BigDecimal("0");
        //Resolve summation
        for (int i = 0; i < j.intValue(); i++) {
            BigDecimal auxSummation = ce.multiply(ds).multiply(ekmtot).add(cdem).add(cb.multiply(nbr.divide(ts, 2, RoundingMode.HALF_EVEN)).multiply(new BigDecimal("1").subtract(drate).pow(-i, MathContext.DECIMAL32)));
            summationValue = summationValue.add(auxSummation);
        }
        return nc.multiply(cv.add(cb)).add(cc).add(summationValue).divide(nc,  2, RoundingMode.HALF_EVEN);
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
     * @param btc      - battery capacity
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
    public static List<BigDecimal> calculateAllValue(String tr, String tint, String eavg,
                                                     String btc, String bped, String phvacavg,
                                                     String d, String emax, String ncgh,
                                                     String ds, String bn, String cv,
                                                     String cb, String cc, String ce,
                                                     String cdem, String ts, String drate,
                                                     String j) {

        //Number of buses (nc)
        BigDecimal nc = calculateNc(new BigDecimal(tr), new BigDecimal(tint));

        //Wait time used for charging
        BigDecimal tw = calculateTw(nc, new BigDecimal(tr), new BigDecimal(tint));

        //Total trip time
        BigDecimal tort = calculateTrtot(new BigDecimal(tr), tw);

        //Total energy consumed on a predetermined route
        BigDecimal ekmtot = calculateEkmTot(new BigDecimal(eavg), new BigDecimal(btc), new BigDecimal(bped), new BigDecimal(phvacavg), new BigDecimal(tr), new BigDecimal(d));

        //Charging power
        BigDecimal pchg = calculatePchg(new BigDecimal(emax), new BigDecimal(d), tw, new BigDecimal(ncgh));

        //Number of battery replacements during a bus’s service life
        BigDecimal nbr = calculateNbr(new BigDecimal(ds), new BigDecimal(bn), new BigDecimal(btc), ekmtot);

        //Total cost of ownership of an individual bus
        BigDecimal ncbus = calculateCBus(nc, new BigDecimal(cv), new BigDecimal(cb), new BigDecimal(cc), new BigDecimal(ce), new BigDecimal(ds), ekmtot, new BigDecimal(cdem), nbr, new BigDecimal(ts), new BigDecimal(drate), new BigDecimal(j));

        return Arrays.asList(nc, tw, tort, ekmtot, pchg, nbr, ncbus);
    }

}
