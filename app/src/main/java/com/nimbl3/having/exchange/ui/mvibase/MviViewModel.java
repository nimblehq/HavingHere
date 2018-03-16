package com.nimbl3.having.exchange.ui.mvibase;

import io.reactivex.Observable;

public interface MviViewModel<I extends MviIntent, S extends MviViewState> {
    void processIntents(Observable<I> intents);

    Observable<S> states();
}
