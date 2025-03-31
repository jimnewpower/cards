package dev.newpower.cards.sim;

public class TimeTracker {
    private long startTime;
    private long endTime;
    private boolean isRunning;

    public TimeTracker() {
        this.startTime = 0;
        this.endTime = 0;
        this.isRunning = false;
    }

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            endTime = System.currentTimeMillis();
            isRunning = false;
        }
    }

    public long getElapsedTimeMillis() {
        if (isRunning) {
            return System.currentTimeMillis() - startTime;
        }
        return endTime - startTime;
    }

    public String getElapsedTimeFormatted() {
        long elapsedMs = getElapsedTimeMillis();
        long seconds = (elapsedMs / 1000) % 60;
        long minutes = (elapsedMs / (1000 * 60)) % 60;
        long hours = (elapsedMs / (1000 * 60 * 60));

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
