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

package com.intel.cosbench.api.auth;

import static com.intel.cosbench.api.auth.AuthConstants.*;

import com.intel.cosbench.api.context.*;
import com.intel.cosbench.api.ioengine.IOEngineAPI;
//import com.intel.cosbench.api.stats.StatsListener;
//import com.intel.cosbench.api.validator.ResponseValidator;
import com.intel.cosbench.config.Config;
import com.intel.cosbench.log.Logger;

/**
 * This class encapsulates one none authentication mechanism which is used if no
 * any other authentication mechanism is assigned.
 * 
 * @author ywang19, qzheng7
 * 
 */
public class NoneAuth implements AuthAPI {

    public static final String API_TYPE = "none";

    protected volatile Context parms;
    protected Logger logger;
    
    protected IOEngineAPI ioengine;
//    protected StatsListener listener;
//    protected ResponseValidator validator;

    /* configurations */
    private boolean logging; // enable logging

    public NoneAuth() {
        /* empty */
    }

//    @Override
//    public void setListener(StatsListener listener) {
//    	this.listener = listener;
//    }

//    @Override
//    public void setValidator(ResponseValidator validator) {
//    	this.validator = validator;
//    }
    
    @Override
    public IOEngineAPI initIOEngine(IOEngineAPI ioengine) {
    	return this.ioengine = ioengine;
    }
    
    @Override
    public void init(Config config, Logger logger) {
        this.logger = logger;
        this.parms = new Context();
        
        logging = config.getBoolean(LOGGING_KEY, LOGGING_DEFAULT);
        /* register all parameters */
        parms.put(LOGGING_KEY, logging);
    }

    @Override
    public void dispose() {
        /* empty */
    }

    @Override
    public Context getParms() {
        return parms;
    }

    @Override
    public ExecContext login() {
        if (logging)
            logger.info("perform authentication");
        return null;
    }

}
