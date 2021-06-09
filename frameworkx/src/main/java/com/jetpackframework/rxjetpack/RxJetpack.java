package com.jetpackframework.rxjetpack;

public class RxJetpack {
    private Function<Observable,Observable> onObservableAssembly;
    private static RxJetpack rxJetpack;
    static {
        rxJetpack = new RxJetpack();
        reset();
    }
    private RxJetpack(){

    }
    public static <T> Observable<T> onAssembly(Observable<T> source) {
        if (rxJetpack.onObservableAssembly != null) {
            try {
                return rxJetpack.onObservableAssembly.apply(source);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return source;
    }
    public static RxJetpack setOnObservableAssembly(Function<Observable,Observable> function){
        rxJetpack.onObservableAssembly = function;
        return rxJetpack;
    }
    public static void reset(){
        rxJetpack.setOnObservableAssembly(null);
    }
}
