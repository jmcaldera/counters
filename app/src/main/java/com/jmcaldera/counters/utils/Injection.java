package com.jmcaldera.counters.utils;

import com.jmcaldera.counters.data.CountersRepository;
import com.jmcaldera.counters.data.remote.RemoteDataSource;

/**
 * Created by jmcaldera on 05/12/2017.
 */

public class Injection {

    public static CountersRepository provideCountersRepository() {

        return CountersRepository.getInstance(RemoteDataSource.getInstance());
    }
}
