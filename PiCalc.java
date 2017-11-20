import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class PiCalc {
	public static void main (String[] args) {
		long numThreads = Long.valueOf(args[0]);
		long totalIterations = Long.valueOf(args[1]);
		AtomicLong insideCircle = new AtomicLong();

		PiThread[] ts = new PiThread[(int) numThreads];

		for (int i = 0; i < numThreads; i++) {
			ts[i] = new PiThread(insideCircle, totalIterations/numThreads);
		}

		for (int i = 0; i < numThreads; i++) {
			ts[i].start();
		}

		for (int i = 0; i < numThreads; i++) {
			try {
				ts[i].join();
			} catch (InterruptedException iex) {}
		}
		System.out.println("Total\t= " + totalIterations);
		System.out.println("Inside\t= " + insideCircle);
		double ratio = insideCircle.floatValue() / totalIterations;
		System.out.println("Ratio\t= " + ratio);
		System.out.println("Pi\t= " + (ratio * 4));


	}

	private static class PiThread extends Thread {
		long iterationsPerThread;
		AtomicLong insideCircle;

		private PiThread(AtomicLong i, long num) {
			insideCircle = i;
			iterationsPerThread = num;
		}

		public void run() {
			for (int i = 0; i < iterationsPerThread; i++) {
				double x, y;
				x = ThreadLocalRandom.current().nextDouble();
				y = ThreadLocalRandom.current().nextDouble();
				if (Math.pow(x, 2) + Math.pow(y, 2) < 1) {
					insideCircle.incrementAndGet();
				}
			}
		}
	}
}
