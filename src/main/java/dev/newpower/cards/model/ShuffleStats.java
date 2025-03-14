package dev.newpower.cards.model;

public class ShuffleStats {
    private int nJavaShuffles = 0;
    private int nCuts = 0;
    private int nRiffles = 0;

    public ShuffleStats() {
    }

    @Override
    public String toString() {
        return "ShuffleStats{" +
                "nJavaShuffles=" + nJavaShuffles +
                ", nCuts=" + nCuts +
                ", nRiffles=" + nRiffles +
                '}';
    }

    public void reset() {
        nJavaShuffles = 0;
        nCuts = 0;
        nRiffles = 0;
    }

    public void incrementJavaShuffles() {
        this.nJavaShuffles++;
    }

    public void incrementCuts() {
        this.nCuts++;
    }

    public void incrementRiffles() {
        this.nRiffles++;
    }

    public int getnJavaShuffles() {
        return nJavaShuffles;
    }

    public void setnJavaShuffles(int nJavaShuffles) {
        this.nJavaShuffles = nJavaShuffles;
    }

    public int getnCuts() {
        return nCuts;
    }

    public void setnCuts(int nCuts) {
        this.nCuts = nCuts;
    }

    public int getnRiffles() {
        return nRiffles;
    }

    public void setnRiffles(int nRiffles) {
        this.nRiffles = nRiffles;
    }
}
