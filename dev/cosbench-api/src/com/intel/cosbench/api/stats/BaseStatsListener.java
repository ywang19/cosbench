package com.intel.cosbench.api.stats;

import com.intel.cosbench.api.context.ExecContext;
import com.intel.cosbench.log.LogFactory;
import com.intel.cosbench.log.Logger;

/**
 * The stats collector is used to count run-time performance stats.
 * 
 * @author ywang19
 *
 */
public class BaseStatsListener extends StatsListener {
	
	private long ts_start;
	private long ts_end;
	
    private static final Logger LOGGER = LogFactory.getSystemLogger();
	
	public BaseStatsListener() {
		this.ts_start = System.currentTimeMillis();
	}
	
	public BaseStatsListener(long timestamp) {
		this.ts_start = timestamp;
	}
	
	@Override
	public void onStats(ExecContext context, boolean status) {
		this.ts_end = System.currentTimeMillis();
		LOGGER.debug("Request is " + (status? "succeed" : "failed") + " in " + (ts_end - ts_start) + " milliseconds.");
	}
	   
}
