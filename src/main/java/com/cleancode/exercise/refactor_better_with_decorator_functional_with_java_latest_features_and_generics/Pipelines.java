package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features_and_generics;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class Pipelines {
    private Pipelines() {}

    // Three-stage convenience: LOG -> RETRY -> EMAIL_ASYNC
    public static <K extends Enum<K>, T> T threeStage(
            T base,
            DecoratorRegistries<K, T> regs,
            K loggingKey,
            K retryKey,
            K emailAsyncKey
    ) {
        var p = apply(base, regs.basic().get(loggingKey));
        p = apply(p, regs.basic().get(retryKey));
        p = apply(p, regs.email().get(emailAsyncKey));
        return p;
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