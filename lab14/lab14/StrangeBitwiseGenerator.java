package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int state;
    private int period;
    private double range;
    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        this.state = 0;
        this.range = (float) 2 / (period - 1);
    }

    @Override
    public double next() {
        int i = state % period;
        state++;
        int weirdState = state & (state >>> 3) % period;
        return (weirdState % period) * range - 1;
    }
}
