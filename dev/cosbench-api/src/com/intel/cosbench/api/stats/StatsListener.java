package com.intel.cosbench.api.stats;

import com.intel.cosbench.api.context.ExecContext;

public abstract class StatsListener {

	public abstract void onStats(ExecContext context, boolean status); 
	
}
