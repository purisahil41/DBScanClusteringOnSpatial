package DBScanPackage;

class Stopwatch {
    private long startTime;
    private long stopTime;
    private boolean isRunning = false;
    private long totalTime = 0;

    public void start() {
        if (isRunning) {
            return;
        }
        startTime = System.nanoTime();
        isRunning = true;
    }

    public void stop() {
        if (!isRunning) {
            return;
        }
        stopTime = System.nanoTime();
        totalTime += stopTime - startTime; // increment the cumulated time
        isRunning = false;
    }

    public void reset() {
        isRunning = false; // stop the watch
        totalTime = 0; // erase the previously cumulated time
    }

    public double getElapsedSeconds() {
        return toSeconds(getElapsedNanoseconds());
    }

    public long getElapsedMilliseconds() {
        return toMilliseconds(getElapsedNanoseconds());
    }

    public long getElapsedNanoseconds() {
        if (isRunning) {
            return System.nanoTime() - startTime + totalTime;
        } else {
            return totalTime;
        }
    }

    private static double toSeconds(long time) {
        return time * 1E-9; // convert nanoseconds into seconds
    }

    private static long toMilliseconds(long time) {
        return time / 1000000; // convert nanoseconds into milliseconds
    }
}
