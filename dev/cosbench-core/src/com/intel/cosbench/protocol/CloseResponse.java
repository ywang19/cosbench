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

package com.intel.cosbench.protocol;

import java.util.ArrayList;
import java.util.List;

import com.intel.cosbench.bench.Metrics;

/**
 * The response to get log from driver when closed.
 * 
 * @author ywang19, qzheng7
 *
 */
public class CloseResponse extends Response {

    private List<Metrics> report; /* metrics report */
    private String driverLog; /* driver log */

    public CloseResponse() {
        super();
    }

    public CloseResponse(int code, String error) {
    	super(code,error);
    }
    
    public List<Metrics> getReport() {
    	return report != null? report: new ArrayList<Metrics>();
    }

    public void setReport(List<Metrics> report) {
        this.report = report;
    }

    public String getDriverLog() {
        return driverLog;
    }

    public void setDriverLog(String log) {
        this.driverLog = log;
    }

}
