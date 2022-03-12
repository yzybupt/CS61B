package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int state;
    private int period;
    private double range;
    private double factor;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.state = 0;
        this.factor = factor;
        this.range = (float) 2 / (period - 1);
    }

    @Override
    public double next() {
        if (state == period) {
            flush();
        }
        double results = state * range - 1;
        state++;
        return results;
    }

    private void flush() {
        state = 0;
        period = (int) (period * factor);
        range = (float) 2 / (period - 1);

    }

}
