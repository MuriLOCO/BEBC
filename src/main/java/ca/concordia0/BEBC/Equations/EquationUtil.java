package ca.concordia0.BEBC.Equations;

import com.fasterxml.jackson.databind.node.BigIntegerNode;

import java.math.BigInteger;

public class EquationUtil {

    private static BigInteger calculateNc(BigInteger tr, BigInteger tint){
        return tr.divide(tint);
    }

    private static BigInteger calculateTw(BigInteger nc, BigInteger tr, BigInteger tint){
        return nc.multiply(tint).min(tr);
    }

    private static BigInteger calculateTrtot(BigInteger tr, BigInteger tw){
        return tr.add(tw);
    }

    private static BigInteger calculateEkmTot(BigInteger eAvg, BigInteger bTc, BigInteger bPed, BigInteger pHvacAvg, BigInteger tr, BigInteger d){
        BigInteger firstPart = eAvg.add(new BigInteger("0.1").multiply(bTc.divide(bPed)));
        BigInteger secondPart = pHvacAvg.multiply(tr.divide(new BigInteger("3600").divide(d)));
        return firstPart.add(secondPart);
    }

    private static BigInteger calculatePchg(BigInteger eMax, BigInteger d, BigInteger tW, BigInteger tw, BigInteger nchg){
        return new BigInteger("3600").multiply(eMax).multiply(d).divide(tw.multiply(nchg));
    }

    private static BigInteger calculateNbr(BigInteger ds, BigInteger bn, BigInteger btc, BigInteger ekmTot){
        return ds.multiply(bn.multiply(btc).divide(ekmTot));
    }
}
