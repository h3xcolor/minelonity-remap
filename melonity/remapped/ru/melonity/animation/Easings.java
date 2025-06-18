// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Easings {
    public static final double BACK_CONSTANT = 1.70158;
    public static final double BACK_BOTH_CONSTANT = 2.5949095;
    public static final double BACK_CONSTANT_PLUS_ONE = 2.70158;
    public static final double ELASTIC_TWO_PI_OVER_THREE = 2.0943951023931953;
    public static final double ELASTIC_TWO_PI_OVER_4_5 = 1.3962634015954636;
    public static final Easing BACK_BOTH = progress -> {
        if (progress < 0.5) {
            double scaled = 2.0 * progress;
            double squared = scaled * scaled;
            return squared * (7.189819 * scaled - BACK_BOTH_CONSTANT) / 2.0;
        }
        double scaled = 2.0 * progress - 2.0;
        double squared = scaled * scaled;
        return (squared * (3.5949095 * scaled + BACK_BOTH_CONSTANT) + 2.0) / 2.0;
    };
    public static final Easing BACK_IN = progress -> BACK_CONSTANT_PLUS_ONE * Math.pow(progress, 3.0) - BACK_CONSTANT * Math.pow(progress, 2.0);
    public static final Easing BACK_OUT = progress -> 1.0 + BACK_CONSTANT_PLUS_ONE * Math.pow(progress - 1.0, 3.0) + BACK_CONSTANT * Math.pow(progress - 1.0, 2.0);
    public static final Easing LINEAR = progress -> progress;
    public static final Easing QUAD_IN = Easings.powIn(2);
    public static final Easing QUAD_OUT = Easings.powOut(2);
    public static final Easing QUAD_BOTH = Easings.powBoth(2.0);
    public static final Easing CUBIC_IN = Easings.powIn(3);
    public static final Easing CUBIC_OUT = Easings.powOut(3);
    public static final Easing CUBIC_BOTH = Easings.powBoth(3.0);
    public static final Easing QUART_IN = Easings.powIn(4);
    public static final Easing QUART_OUT = Easings.powOut(4);
    public static final Easing QUART_BOTH = Easings.powBoth(4.0);
    public static final Easing QUINT_IN = Easings.powIn(5);
    public static final Easing QUINT_OUT = Easings.powOut(5);
    public static final Easing QUINT_BOTH = Easings.powBoth(5.0);
    public static final Easing SINE_IN = progress -> 1.0 - Math.cos(progress * Math.PI / 2.0);
    public static final Easing SINE_OUT = progress -> Math.sin(progress * Math.PI / 2.0);
    public static final Easing SINE_BOTH = progress -> -(Math.cos(Math.PI * progress) - 1.0) / 2.0;
    public static final Easing CIRC_IN = progress -> 1.0 - Math.sqrt(1.0 - Math.pow(progress, 2.0));
    public static final Easing CIRC_OUT = progress -> Math.sqrt(1.0 - Math.pow(progress - 1.0, 2.0));
    public static final Easing CIRC_BOTH = progress -> {
        if (progress < 0.5) {
            double scaled = 2.0 * progress;
            double squared = scaled * scaled;
            double root = Math.sqrt(1.0 - squared);
            return (1.0 - root) / 2.0;
        }
        return (Math.sqrt(1.0 - Math.pow(-2.0 * progress + 2.0, 2.0)) + 1.0) / 2.0;
    };
    public static final Easing ELASTIC_IN = progress -> {
        if (progress == 0.0 || progress == 1.0) {
            return progress;
        }
        return Math.pow(-2.0, 10.0 * progress - 10.0) * Math.sin((progress * 10.0 - 10.75) * ELASTIC_TWO_PI_OVER_THREE);
    };
    public static final Easing ELASTIC_OUT = progress -> {
        if (progress == 0.0 || progress == 1.0) {
            return progress;
        }
        return Math.pow(2.0, -10.0 * progress) * Math.sin((progress * 10.0 - 0.75) * ELASTIC_TWO_PI_OVER_THREE) + 1.0;
    };
    public static final Easing ELASTIC_BOTH = progress -> {
        if (progress == 0.0 || progress == 1.0) {
            return progress;
        }
        if (progress < 0.5) {
            double factor = Math.pow(2.0, 20.0 * progress - 10.0);
            double sine = Math.sin((20.0 * progress - 11.125) * ELASTIC_TWO_PI_OVER_4_5);
            return -(factor * sine) / 2.0;
        }
        return Math.pow(2.0, -20.0 * progress + 10.0) * Math.sin((20.0 * progress - 11.125) * ELASTIC_TWO_PI_OVER_4_5) / 2.0 + 1.0;
    };
    public static final Easing EXPO_IN = progress -> {
        if (progress == 0.0) {
            return progress;
        }
        return Math.pow(2.0, 10.0 * progress - 10.0);
    };
    public static final Easing EXPO_OUT = progress -> {
        if (progress == 1.0) {
            return progress;
        }
        return 1.0 - Math.pow(2.0, -10.0 * progress);
    };
    public static final Easing EXPO_BOTH = progress -> {
        if (progress == 0.0 || progress == 1.0) {
            return progress;
        }
        if (progress < 0.5) {
            return Math.pow(2.0, 20.0 * progress - 10.0) / 2.0;
        }
        return (2.0 - Math.pow(2.0, -20.0 * progress + 10.0)) / 2.0;
    };
    public static final Easing BOUNCE_OUT = progress -> {
        double baseFactor = 7.5625;
        double segment = 2.75;
        if (progress < 1.0 / segment) {
            return baseFactor * progress * progress;
        } else if (progress < 2.0 / segment) {
            double adjusted = progress - 1.5 / segment;
            return baseFactor * adjusted * adjusted + 0.75;
        } else if (progress < 2.5 / segment) {
            double adjusted = progress - 2.25 / segment;
            return baseFactor * adjusted * adjusted + 0.9375;
        }
        double adjusted = progress - 2.625 / segment;
        return baseFactor * adjusted * adjusted + 0.984375;
    };
    public static final Easing BOUNCE_IN = progress -> 1.0 - BOUNCE_OUT.ease(1.0 - progress);
    public static final Easing BOUNCE_BOTH = progress -> {
        if (progress < 0.5) {
            return (1.0 - BOUNCE_OUT.ease(1.0 - 2.0 * progress)) / 2.0;
        }
        return (1.0 + BOUNCE_OUT.ease(2.0 * progress - 1.0)) / 2.0;
    };

    private Easings() {
    }

    public static Easing powIn(double exponent) {
        return progress -> Math.pow(progress, exponent);
    }

    public static Easing powIn(int exponent) {
        return powIn((double) exponent);
    }

    public static Easing powOut(double exponent) {
        return progress -> 1.0 - Math.pow(1.0 - progress, exponent);
    }

    public static Easing powOut(int exponent) {
        return powOut((double) exponent);
    }

    public static Easing powBoth(double exponent) {
        return progress -> {
            if (progress < 0.5) {
                double factor = Math.pow(2.0, exponent - 1.0);
                double powered = Math.pow(progress, exponent);
                return factor * powered;
            }
            return 1.0 - Math.pow(-2.0 * progress + 2.0, exponent) / 2.0;
        };
    }
}