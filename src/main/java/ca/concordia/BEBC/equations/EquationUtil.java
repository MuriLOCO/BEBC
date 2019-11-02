package ca.concordia.BEBC.equations;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class EquationUtil {

    private static BigInteger calculateNc(BigInteger tr, BigInteger tint){
        return tr.divide(tint);
    }

    private static BigInteger calculateTw(BigInteger nc, BigInteger tr, BigInteger tint){
        return nc.multiply(tint).subtract(tr);
    }

    private static BigInteger calculateTrtot(BigInteger tr, BigInteger tw){
        return tr.add(tw);
    }

    public static BigInteger calculateEkmTot(BigInteger eAvg, BigInteger bTc, BigInteger bPed, BigInteger pHvacAvg, BigInteger tr, BigInteger d){
        BigInteger firstPart = eAvg.add(new BigInteger("0.1").multiply(bTc.divide(bPed)));
        BigInteger secondPart = pHvacAvg.multiply(tr.divide(new BigInteger("3600").divide(d)));
        return firstPart.add(secondPart);
    }

    public static BigInteger calculatePchg(BigInteger eMax, BigInteger d, BigInteger tW, BigInteger tw, BigInteger nchg){
        return new BigInteger("3600").multiply(eMax).multiply(d).divide(tw.multiply(nchg));
    }

    public static BigInteger calculateNbr(BigInteger ds, BigInteger bn, BigInteger btc, BigInteger ekmTot){
        return ds.multiply(bn.multiply(btc).divide(ekmTot));
    }

    public static BigInteger calculateCBus(BigInteger nc, BigInteger cv, BigInteger cb, BigInteger cc, BigInteger ce, BigInteger ds, BigInteger ekmTot, BigInteger cdem, BigInteger nbr, BigInteger ts, BigInteger dRate, BigInteger j){
        BigInteger summationValue = new BigInteger("0");
        //Resolve summation
        for(int i = 0; i < j.intValue(); i++){
        BigInteger auxSummation = ce.multiply(ds.multiply(ekmTot)).add(cdem).add(cb.multiply(nbr.divide(ts)).multiply(new BigInteger("1").subtract(dRate).pow(-i)));
        summationValue = summationValue.add(auxSummation);
        }
        return nc.multiply(cv.add(cb)).add(summationValue).divide(nc);
    }


    /**
     * Outputs number of buses (Nc), wait time used for charging (tw) and total trip time (trtot) as a list of BigIntegers (follows this order)
     * Other parameters are:
     * nc - number of buses
     * tw - wait time used for charging
     * @param tr - time spent driving
     * @param tint - bus schedule interval
     * @return
     */
    public static List<BigInteger> calculateNcTwTrtot(BigInteger tr, BigInteger tint){
        BigInteger nc = calculateNc(tr, tint);
        BigInteger tw = calculateTw(nc, tr, tint);
        BigInteger tort = calculateTrtot(tr, tw);

        return Arrays.asList(nc, tw, tort);
    }
}
