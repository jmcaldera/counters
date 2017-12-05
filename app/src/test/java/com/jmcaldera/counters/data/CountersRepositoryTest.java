package com.jmcaldera.counters.data;

import com.google.common.collect.Lists;
import com.jmcaldera.counters.data.model.Counter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by jmcaldera on 04/12/2017.
 */

public class CountersRepositoryTest {

    private static final String COUNTER_TITLE1 = "bob";
    private static final String COUNTER_TITLE2 = "steve";

    private static List<Counter> COUNTERS = Lists.newArrayList(new Counter(0, "asdf", COUNTER_TITLE1),
            new Counter(0, "qwer", COUNTER_TITLE2));

    private CountersRepository mCountersRepository;

    @Mock
    private DataSource mRemoteDataSource;

    @Mock
    private DataSource.LoadCountersCallback mCallback;

    @Captor
    private ArgumentCaptor<DataSource.LoadCountersCallback> mCallbackCaptor;

    @Before
    public void setupCountersRepository() {
        // Inject the mocks
        MockitoAnnotations.initMocks(this);

        mCountersRepository = CountersRepository.getInstance(mRemoteDataSource);
    }

    @After
    public void destroyCountersRepository() {
        CountersRepository.destroyInstance();
    }

    @Test
    public void getCounters_repositoryAddsResponseToCacheAfterApiCall() {
        mCountersRepository.getCounters(mCallback);
        verify(mRemoteDataSource).getCounters(mCallbackCaptor.capture());
        mCallbackCaptor.getValue().onSuccess(COUNTERS);
        verify(mRemoteDataSource).getCounters(any(DataSource.LoadCountersCallback.class));
    }

    @Test
    public void addCounter_addCounterToRemoteDataSource() {
        String title = "bob";
        mCountersRepository.addCounter(title, mCallback);
        verify(mRemoteDataSource).addCounter(eq(title), mCallbackCaptor.capture());
        mCallbackCaptor.getValue().onSuccess(COUNTERS);
        verify(mRemoteDataSource).addCounter(any(String.class), any(DataSource.LoadCountersCallback.class));
    }
}
