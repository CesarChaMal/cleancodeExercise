// Java
package com.cleancode.exercise.refactor_better_with_decorator_functional_with_java_latest_features;

public final class RichDecorators {
    private RichDecorators() {}

    public static RichOrder withEmail(RichOrder base, EmailSender emailSender) {
        return customer -> {
            base.process(customer);
            var email = customer.email();
            if (email != null && !email.isBlank()) {
                emailSender.send(customer.name());
            }
        };
    }
}