package graph;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Currency exchange rates are quoted as currency pairs, such as EURUSD or USDAUD.
 * The first currency in the pair is referred to as the base currency, and the second
 * currency is called the quote currency.
 * For example, EURUSD quoted at $1.17 tells us that 1 EUR will buy you 1.17 USD.
 * This rate is one way only, and does not tell us how many EUR one USD would buy.
 *
 * If you were to visit a foreign exchange broker to buy NZD for a trip to New Zealand,
 * but the broker didn't quote AUDNZD, you may still be able to get your NZD by executing
 * multiple trades. For example you could trade AUDUSD to buy some USD, and then trade
 * USDNZD to buy NZD with your newly purchased USD, assuming these currency pairs were available.
 *
 *
 * Problem:
 * We want you to design and implement an ExchangeRateCalculator  class that can calculate
 * the cash value a given amount of base currency is worth in a specified quote currency,
 * e.g. calculate that $100 USD is worth $130 AUD if USDAUD is 1.3.
 *
 * You will be given a function stub calculate(...) which will be used to drive the unit tests we've prepared. This function should interact with your class.
 * You will be given an amount of base currency to exchange into the quote currency (ccy_value), e.g. $1000 USD to be converted to AUD.
 * You will be given the exchange rate you need to calculate for (ccy_pair) in the format <base><quote>, e.g. AUDEUR
 * You will be given a list of available exchange rates (ccy_rates) as string's in the format <base><quote>@<price>, e.g. AUDEUR@0.64.
 * You should return your result rounded down (floored) to the nearest whole unit of currency.
 * You should return -1 if no valid exchange rate exists.
 * You can assume all inputs are valid.
 */
public class ExchangeRateCalculator {
    static String input = "1000000\n" +
            "AUDGBP\n" +
            "6\n" +
            "AUDEUR@0.6166\n" +
            "AUDCNY@4.715\n" +
            "CNYJPY@17.08\n" +
            "EURJPY@129.53\n" +
            "JPYGBP@0.006614\n" +
            "GBPCNY@8.852";

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(input));

        int ccy_value = Integer.parseInt(bufferedReader.readLine().trim());

        String ccy_pair = bufferedReader.readLine();

        int ccy_ratesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> ccy_rates = IntStream.range(0, ccy_ratesCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        int result = Result.calculate(ccy_value, ccy_pair, ccy_rates);

        System.out.println(result);
        bufferedReader.close();
    }

    static class Result {
        /**
         * Complete the 'calculate' function below.
         *
         * The function is expected to return an INTEGER.
         * The function accepts following parameters:
         *  1. INTEGER ccy_value
         *  2. STRING ccy_pair
         *  3. STRING_ARRAY ccy_rates
         */
        public static int calculate(int ccy_value, String ccy_pair, List<String> ccy_rates) {
            RateCalculator calculator = new RateCalculator(ccy_rates);
            if (ccy_pair == null || ccy_pair.length()<6) {
                // Invalid Pair
                return -1;
            }
            String src = ccy_pair.substring(0, 3);
            String dest = ccy_pair.substring(3, 6);

            Double rate = calculator.getRate(src, dest);
            if (rate == null) return -1;


            double result = Math.floor(rate * ccy_value);
            System.out.println(src + " " + dest + " " + rate + " " + (int)result);
            return (int)result;
        }
    }

    static class RateCalculator {
        private final Map<String, List<Rate>> rates = new HashMap<>();

        RateCalculator(List<String> ccy_rates) {
            // build rates map
            for (String strRate: ccy_rates) {
                String src = strRate.substring(0, 3);
                String dest = strRate.substring(3, 6);
                String stRate = strRate.split("@")[1];
                Rate rate = new Rate(src, dest, stRate);
                rates.computeIfAbsent(src, s -> new ArrayList<>()).add(rate);
            }
            System.out.println(rates);
        }

        Double getRate (String src, String dest) {
            List<Rate> curRate = rates.get(src);
            List<Rate> nextRates;
            Map<String, Double> encountered = new HashMap<>();
            encountered.put(src, 1.0);
            while (curRate.size() > 0) {
                nextRates = new ArrayList<>();
                for (Rate r: curRate) {
                    if (r.dest.equals(dest)) {
                        // found our result
                        double rate = encountered.get(r.source);
                        return (rate * r.rate);
                    }
                    if (!encountered.containsKey(r.dest)) {
                        // add to new encountered
                        double rate = encountered.get(r.source);
                        double newrate = (rate * r.rate);
                        encountered.put(r.dest, newrate);
                        nextRates.addAll(rates.get(r.dest));
                    }
                }
                curRate = nextRates;
            }
            return null;
        }
    }


    static class Rate {
        String source;
        String dest;
        double rate;

        Rate(String s, String d, String stRate) {
            source = s;
            dest = d;
            rate = Double.parseDouble(stRate);
        }

        @Override
        public String toString() {
            return (source + "->" + dest + " : " + rate);
        }
    }
}
