/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2016 Broad Institute, Aiden Lab
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package juicebox.tools.utils;

import jargs.gnu.CmdLineParser;
import juicebox.HiC;
import juicebox.HiCGlobals;
import juicebox.tools.clt.JuiceboxCLT;
import juicebox.tools.clt.old.Dump;
import org.broad.igv.Globals;
import org.broad.igv.feature.Chromosome;

import java.util.Map;
import java.util.Random;

/**
 * Created by Neva Durand on 8/4/16 for benchmark testing for DCIC.
 */

public class Benchmark extends JuiceboxCLT {

    private Dump dump;
    private String singleChr = null;

    public Benchmark() {
        super(getUsage());
    }

    public static String getUsage(){
        return "benchmark <hicFile> [norm] [chr]";
    }

    @Override
    public void readArguments(String[] argv, CmdLineParser parser)   {
        Globals.setHeadless(true);

        String norm = "NONE";

        if (argv.length == 3 || argv.length == 4) {
            norm = argv[2];
            if (argv.length == 4) singleChr = argv[3];
        }
        else if (argv.length != 2) {
            printUsageAndExit();
        }

        dump = new Dump();

        // dump will read in the index of the .hic file and output the observed matrix with no normalization
        // change "NONE" to "KR" or "VC" for different normalizations
        // change output file if needed
        // the other values are dummy and will be reset
        String[] args = {"dump", "observed", norm, argv[1], "X", "X", "BP", "1000000", "output.txt"};
        dump.readArguments(args, parser);
    }


    @Override
    public void run() {

        // will use to make sure we're not off the end of the chromosome
        Map<String, Chromosome> map = dump.getChromosomeMap();

        Random random = new Random();
        String[] chrs;

        if (singleChr == null)  {
            // chromosomes in this dataset, so we query them
            // exclude whole genome and chrs Y and MT, which don't have a lot of data
            int ind = 0;
            for (String chr : map.keySet()) {
                if (!chr.equals("All") && !chr.equals("Y") && !chr.equals("MT")) ind++;
            }
            chrs = new String[ind];
            ind = 0;
            for (String chr : map.keySet()) {
                if (!chr.equals("All") && !chr.equals("Y") && !chr.equals("MT")) chrs[ind++] = chr;
            }
        }  else {
            chrs = new String[1];
            chrs[0] = singleChr;
        }

        // BP bin sizes in this dataset
        int[] bpBinSizes = dump.getBpBinSizes();

        // Query 10,000 times at 256x256
        int QUERY_SIZE=256;
        int NUM_QUERIES=10000;
        long sum = 0;
        for (int i = 0; i < NUM_QUERIES; i++) {
            // Randomly choose chromosome and resolution to query
            String chr1 = chrs[random.nextInt(chrs.length)];
            int binSize = bpBinSizes[random.nextInt(bpBinSizes.length)];

            int end1 = random.nextInt(map.get(chr1).getLength()); // endpoint between 0 and end of chromosome
            int start1 = end1 - binSize * QUERY_SIZE; // QUERY_SIZE number of bins earlier
            if (start1 < 0) start1 = 0;

            long currentTime = System.currentTimeMillis();
            dump.setQuery(chr1 + ":" + start1 + ":" + end1, chr1 + ":" + start1 + ":" + end1, binSize);
            dump.run();
            long totalTime = System.currentTimeMillis() - currentTime;
            sum += totalTime;
        }
        System.err.println("Average time to query " + QUERY_SIZE + "x" + QUERY_SIZE + ": " + sum / NUM_QUERIES + " milliseconds");

        QUERY_SIZE = 2048;
        NUM_QUERIES = 100;
        sum = 0;
        for (int i = 0; i < NUM_QUERIES; i++) {
            // Randomly choose chromosome and resolution to query
            String chr1 = chrs[random.nextInt(chrs.length)];
            int binSize = bpBinSizes[random.nextInt(bpBinSizes.length)];

            int end1 = random.nextInt(map.get(chr1).getLength()); // endpoint between 0 and end of chromosome
            int start1 = end1 - binSize * QUERY_SIZE; // QUERY_SIZE number of bins earlier
            if (start1 < 0) start1 = 0;

            long currentTime = System.currentTimeMillis();
            dump.setQuery(chr1 + ":" + start1 + ":" + end1, chr1 + ":" + start1 + ":" + end1, binSize);
            dump.run();
            long totalTime = System.currentTimeMillis() - currentTime;
            sum += totalTime;
        }
        System.err.println("Average time to query " + QUERY_SIZE + "x" + QUERY_SIZE + ": " + sum / NUM_QUERIES + " milliseconds");

        // Slice X benchmark
        NUM_QUERIES = 100;
        sum = 0;
        int dataSize = 0;
        for (int i = 0; i < NUM_QUERIES; i++) {
            // Randomly choose chromosome and resolution to query
            String chr1 = chrs[random.nextInt(chrs.length)];
            int binSize = bpBinSizes[random.nextInt(bpBinSizes.length)];

            int end1 = random.nextInt(map.get(chr1).getLength()); // endpoint between 0 and end of chromosome
            end1 = (end1 / binSize) * binSize;

            long currentTime = System.currentTimeMillis();
            float[] xValues = dump.getSlice(chr1 + ":" + end1 + ":" + end1, chr1, binSize);
            long totalTime = System.currentTimeMillis() - currentTime;
            sum += totalTime;
            dataSize += xValues.length;
            if (HiCGlobals.printVerboseComments) {
                System.err.println("Query " + xValues.length + " rows at " + binSize + "BP on chr" + chr1 + ": " + totalTime + " millseconds");
            }
        }
        System.err.println("Average time to query 1000 rows: " + sum / (dataSize / 1000) + " milliseconds");

        // Slice Y benchmark
        sum = 0;
        dataSize = 0;
        for (int i = 0; i < NUM_QUERIES; i++) {
            // Randomly choose chromosome and resolution to query
            String chr1 = chrs[random.nextInt(chrs.length)];
            int binSize = bpBinSizes[random.nextInt(bpBinSizes.length)];

            int end1 = random.nextInt(map.get(chr1).getLength()); // endpoint between 0 and end of chromosome
            end1 = (end1 / binSize) * binSize;

            long currentTime = System.currentTimeMillis();
            float[] yValues = dump.getSlice(chr1 + ":" + end1 + ":" + end1, chr1, binSize);
            long totalTime = System.currentTimeMillis() - currentTime;
            sum += totalTime;
            dataSize += yValues.length;
            if (HiCGlobals.printVerboseComments) {
                System.err.println("Query " + yValues.length + " columns at " + binSize + "BP on chr" + chr1 + ": " + totalTime + " millseconds");
            }
        }
        System.err.println("Average time to query 1000 columns: "+sum/(dataSize/ 1000) + " milliseconds");

        // Slice diagonal benchmark
        sum = 0;
        dataSize = 0;
        NUM_QUERIES = 10;
        for (int i = 0; i < NUM_QUERIES; i++) {
            // Randomly choose chromosome and resolution to query
            String chr1 = chrs[random.nextInt(chrs.length)];
            int binSize = bpBinSizes[random.nextInt(bpBinSizes.length)];

            long currentTime = System.currentTimeMillis();
            float[] yValues = dump.getSlice(chr1, chr1, binSize);
            long totalTime = System.currentTimeMillis() - currentTime;
            sum += totalTime;
            dataSize += yValues.length;
            if (HiCGlobals.printVerboseComments) {
                System.err.println("Query " + yValues.length + "-length diagonal at " + binSize + "BP on chr" + chr1 + ": " + totalTime + " millseconds");
            }
        }
        System.err.println("Average time to query 1000 diagonal entries: "+sum/(dataSize/ 1000) + " milliseconds");
    }
}
