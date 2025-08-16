package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;

import java.util.Map;
import java.util.function.UnaryOperator;

public final class Pipelines {
    private Pipelines() {}

    // Generic 3-stage pipeline: STAGE1 -> STAGE2 -> EMAIL
    // Names are neutral to work with both TIMING/RETRY and EMAIL_SYNC/ASYNC keys.
    public static <K extends Enum<K>, T> T threeStage(
            T base,
            DecoratorRegistries<K, T> regs,
            K stage1Key,
            K stage2Key,
            K emailKey
    ) {
        var p = apply(base, regs.basic().get(stage1Key));
        p = apply(p, regs.basic().get(stage2Key));
        p = apply(p, regs.email().get(emailKey));
        return p;
    }

    // Convenience: LOGGING -> TIMING -> EMAIL (SYNC/ASYNC)
    public static <K extends Enum<K>, T> T loggingTimingEmail(
            T base,
            DecoratorRegistries<K, T> regs,
            K loggingKey,
            K timingKey,
            K emailKey
    ) {
        return threeStage(base, regs, loggingKey, timingKey, emailKey);
    }

    // Convenience: LOGGING -> RETRY -> EMAIL (SYNC/ASYNC)
    public static <K extends Enum<K>, T> T loggingRetryEmail(
            T base,
            DecoratorRegistries<K, T> regs,
            K loggingKey,
            K retryKey,
            K emailKey
    ) {
        return threeStage(base, regs, loggingKey, retryKey, emailKey);
    }

    // General-purpose varargs builder (all stages from one map)
    @SafeVarargs
    public static <K, T> T build(T base, Map<K, UnaryOperator<T>> stageMap, K... stages) {
        var p = base;
        for (K k : stages) {
            p = apply(p, stageMap.get(k));
        }
        return p;
    }

    private static <T> T apply(T base, UnaryOperator<T> op) {
        return op.apply(base);
    }
}