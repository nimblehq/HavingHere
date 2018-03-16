package com.nimbl3.having.exchange;

import com.nimbl3.having.exchange.ui.HavingApplication;
import com.nimbl3.having.exchange.ui.database.FirebaseDbHelper;
import com.nimbl3.having.exchange.ui.intents.DemandListIntents;
import com.nimbl3.having.exchange.ui.model.Demand;
import com.nimbl3.having.exchange.ui.viewmodel.ListDemandViewModel;
import com.nimbl3.having.exchange.ui.viewstate.DemandListViewState;
import com.nimbl3.having.exchange.ui.viewstate.DemandListViewStateWithData;
import com.nimbl3.having.exchange.ui.viewstate.DemandListviewStateEmpty;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

public class ListDemandViewModelTest {
    @Mock
    HavingApplication mockApplication;
    FirebaseDbHelper dbHelperPositive;
    FirebaseDbHelper dbHelperNegative;

    @Before
    public void setUp() throws Exception {
        dbHelperPositive = new FirebaseDbHelper() {
            @Override
            public int getUser() {
                return 1;
            }

            @Override
            public Demand getDemand() {
                return new Demand("title", "description");
            }
        };
        dbHelperNegative = new FirebaseDbHelper() {
            @Override
            public int getUser() {
                return 0;
            }

            @Override
            public Demand getDemand() {
                return new Demand("title", "description");
            }
        };
    }

    @Test
    public void initialize_shouldShowDemandsWhenUserHasDemand() throws Exception {
        ListDemandViewModel listDemandViewModel = new ListDemandViewModel(mockApplication, dbHelperNegative);
        TestObserver<DemandListViewState> states = listDemandViewModel.states().test();
        listDemandViewModel.processIntents(Observable.just(new DemandListIntents() {
        }));
        states.assertNoErrors()
            .assertValueCount(2)
            // default state
            .assertValueAt(0, demandListViewState -> demandListViewState instanceof DemandListviewStateEmpty)
            .assertValueAt(1, demandListViewState -> demandListViewState instanceof DemandListViewStateWithData);
    }


    @Test
    public void initialize_shouldShowEmptyWhenUserHasNoDemand() throws Exception {
        ListDemandViewModel listDemandViewModel = new ListDemandViewModel(mockApplication, dbHelperPositive);
        TestObserver<DemandListViewState> states = listDemandViewModel.states().test();
        listDemandViewModel.processIntents(Observable.just(new DemandListIntents() {
        }));
        states.assertNoErrors()
            .assertValueCount(2)
            // default state
            .assertValueAt(0, demandListViewState -> demandListViewState instanceof DemandListviewStateEmpty)
            .assertValueAt(1, demandListViewState -> demandListViewState instanceof DemandListviewStateEmpty);
    }
}
