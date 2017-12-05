package com.jmcaldera.counters.counters;

import com.google.common.collect.Lists;
import com.jmcaldera.counters.data.CountersRepository;
import com.jmcaldera.counters.data.DataSource;
import com.jmcaldera.counters.data.model.Counter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by jmcaldera on 05/12/2017.
 */

public class CountersPresenterTest {

    private List<Counter> COUNTERS;

    private static final String COUNTER_TITLE1 = "bob";
    private static final String COUNTER_TITLE2 = "steve";

    @Mock
    private CountersContract.View mView;

    @Mock
    private CountersRepository mRepository;

    @Captor
    private ArgumentCaptor<DataSource.LoadCountersCallback> mCallbackCaptor;

    private CountersPresenter mPresenter;

    @Before
    public void setupCountersPresenter() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new CountersPresenter(mView, mRepository);

        // espera que la vista este activa
        when(mView.isActive()).thenReturn(true);

        COUNTERS = Lists.newArrayList(new Counter(0, "asdf", COUNTER_TITLE1),
                new Counter(0, "qwer", COUNTER_TITLE2));
    }

    @Test
    public void createPresenter_setsPresenterToView() {
        mPresenter = new CountersPresenter(mView, mRepository);

        verify(mView).setPresenter(mPresenter);
    }

    @Test
    public void loadCountersFromRepository_LoadIntoView() {
        mPresenter.loadCounters(true);

        verify(mRepository).getCounters(mCallbackCaptor.capture());
        mCallbackCaptor.getValue().onSuccess(COUNTERS);

        // se muestra el progress indicator
        InOrder inOrder = Mockito.inOrder(mView);
        inOrder.verify(mView).setLoadingIndicator(true);
        // se oculta el progress indicator y se muestran los counters
        inOrder.verify(mView).setLoadingIndicator(false);

        ArgumentCaptor<List> showCountersCaptor = ArgumentCaptor.forClass(List.class);
        verify(mView).showCounters(showCountersCaptor.capture());
        assertTrue(showCountersCaptor.getValue().size() == 2);

    }

    @Test
    public void clickOnAddNewCounters_ShowsAddCounter() {
        mPresenter.addNewCounter();
        verify(mView).showAddNewCounter();
    }

}
