package com.openclassroom.devops.orion.microcrm;

final class LogContext {

    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    private LogContext() {
    }

    static String currentMethodName() {
        return STACK_WALKER.walk(frames -> frames.skip(1)
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName)
                .orElse("unknown"));
    }
}