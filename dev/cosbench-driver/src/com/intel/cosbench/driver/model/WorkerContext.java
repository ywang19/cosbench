/** 
 
Copyright 2013 Intel Corporation, All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 
*/ 

package com.intel.cosbench.driver.model;

import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;

import com.intel.cosbench.api.auth.AuthAPI;
import com.intel.cosbench.api.ioengine.IOEngineAPI;
import com.intel.cosbench.api.storage.StorageAPI;
import com.intel.cosbench.bench.*;
import com.intel.cosbench.config.Mission;
//import com.intel.cosbench.driver.operator.OperationListener;
import com.intel.cosbench.driver.operator.Session;
import com.intel.cosbench.log.Logger;
import com.intel.cosbench.model.WorkerInfo;

/**
 * This class encapsulates worker related information.
 * 
 * @author ywang19, qzheng7
 * 
 */
public class WorkerContext implements WorkerInfo, Session {

    private int index;
    private Mission mission;
    private transient Logger logger;
    private transient IOEngineAPI ioengineApi;
    private transient AuthAPI authApi;
    private transient StorageAPI storageApi;

    private volatile boolean error = false;
    private volatile boolean aborted = false;
    /* Each worker starts with an empty report */
    private volatile Report report = new Report();
    /* Each worker has its private random object so as to enhance performance */
    private transient Random random = new Random(RandomUtils.nextLong());

    private WorkStats stats;
    
    public WorkerContext() {
        /* empty */
    	stats = new WorkStats(this);
    }
    
    public void init() {
        storageApi.setListener(stats);
        
        stats.initTimes();
        stats.initLimites();
        stats.initMarks();
    }
    
    @Override
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public WorkStats getStats() {
        return stats;
    }
    
//    @Override
//    public OperationListener getListener() {
//    	return stats;
//    }
    
    public long getRuntime() {
    	return stats.getRuntime();
    }
    
    public boolean isRunning() {
    	return stats.isRunning();
    }
    
    public long updateStats() {
    	return stats.updateStats();
    }
   
    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    
    public IOEngineAPI getIOEngineApi() {
        return ioengineApi;
    }

    public void setIOEngineApi(IOEngineAPI api) {
        this.ioengineApi = api;
    }

    public AuthAPI getAuthApi() {
        return authApi;
    }

    public void setAuthApi(AuthAPI authApi) {
        this.authApi = authApi;
    }

    public StorageAPI getStorageApi() {
        return storageApi;
    }

    public void setStorageApi(StorageAPI storageApi) {
        this.storageApi = storageApi;
    }

    public boolean isError() {
        return error;
    }
    
    public void waitForCompletion(long interval) {
//    	storageApi.abort();
    	stats.waitForCompletion(interval);    	
    	
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    @Override
    public Snapshot getSnapshot() {
    	return stats.doSnapshot();		
    }

    @Override
    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public Random getRandom() {
        return random;
    }

    @Override
    public void disposeRuntime() {
        authApi.dispose();
        authApi = null;
        storageApi.dispose();
        storageApi = null;
        random = null;
        logger = null;
    }

	@Override
	public int getTotalWorkers() {
		return getMission().getTotalWorkers();
	}

	@Override
	public StorageAPI getApi() {
		return storageApi;
	}

	public void setOperatorRegistry(OperatorRegistry operatorRegistry) {
		stats.setOperatorRegistry(operatorRegistry);
	}

}
