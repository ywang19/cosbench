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

package com.intel.cosbench.api.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

//import org.apache.http.HttpResponse;

import com.intel.cosbench.api.context.*;
import com.intel.cosbench.api.ioengine.IOEngineAPI;
import com.intel.cosbench.api.stats.StatsListener;
//import com.intel.cosbench.api.validator.ResponseValidator;
import com.intel.cosbench.config.Config;
import com.intel.cosbench.log.Logger;

public interface StorageAPI {

	/**
	 * 
	 */
	public void setListener(StatsListener listener);
	
//	/**
//	 * 
//	 */
//	public void setValidator(ResponseValidator validator);
	
	/**
	 * Initializes a <code>IOEngine-API</code>.
	 */
	public IOEngineAPI initIOEngine(IOEngineAPI ioengine);
	
    /**
     * Initializes a <code>Storage-API</code> with parameters contained in the
     * given <code>config</code>, whose content depends on the specific storage
     * type. Normally, it will also initialize one client for storage access.
     * 
     * @param config
     *            - one instance from com.intel.cosbench.config.Config, which
     *            includes parameters for authentication, and it will be passed
     *            from execution engine.
     * @param logger
     *            - one instance from com.intel.cosbench.log.Logger, which
     *            delivers logging capabilities to Storage-API, and it will be passed
     *            from execution engine.
     */
    public void init(Config config, Logger logger);

    /**
     * Releases the resources held by this Storage-API.
     */
    public void dispose();

    /**
     * Retrieves parameters and current settings used by the Storage-API.
     * 
     * @return Context - one Context instance which contains all parameters
     *         configured for the storage.
     */
    public Context getParms();

    /**
     * Aborts the execution of an on-going storage operation (HTTP request) if
     * there is one. 
     * The method expects to provide one approach to abort outstanding operations gracefully 
     * when the worker hits some termination criteria.
     */
    public void abort();

    /**
     * Associates authentication context with this Storage-API for further
     * storage operations.
     * 
     * @param info
     *            - one AuthContext instance, normally, it's the result returned
     *            by the <code>login()</code> from the Auth-API.
     */
    public void setAuthContext(ExecContext info);

    /**
     * Downloads an object from a container.
     * 
     * @param container
     *            - the name of a container.
     * @param object
     *            - the name of an object to be downloaded.
     * @param config
     *            - the configuration used for this operation.
     * @return 
     * @throws IOException 
     * @throws StorageException 
     */
    public void getObject(final String opType, String container, String object, Config config) throws StorageException, IOException;

    /**
     * Creates a new container.
     * 
     * @param container
     *            - the name of a container.
     * @param config
     *            - the configuration used for this operation.
     * @throws IOException 
     * @throws StorageException 
     */
    public void createContainer(final String opType, String container, Config config) throws StorageException, IOException;

    /**
     * Uploads an object into a given container.
     * 
     * @param container
     *            - the name of a container.
     * @param object
     *            - the name of an object to be uploaded.
     * @param data
     *            - the inputStream of the object content.
     * @param length
     *            - the length of object content.
     * @param config
     *            - the configuration used for this operation.
     * @throws IOException 
     * @throws StorageException 
     */
    public void createObject(final String opType, String container, String object, InputStream data,
            long length, Config config) throws StorageException, IOException;

    /**
     * Removes a given container.
     * 
     * @param container
     *            - the name of a container to be removed.
     * @param config
     *            - the configuration used for this operation.
     * @throws IOException 
     */
    public void deleteContainer(final String opType, String container, Config config) throws StorageException, IOException;

    /**
     * Deletes a given object.
     * 
     * @param container
     *            - the name of a container.
     * @param object
     *            - the name of an object to be deleted.
     * @param config
     *            - the configuration used for this operation.
     * @throws IOException 
     * @throws StorageException 
     */
    public void deleteObject(final String opType, String container, String object, Config config) throws StorageException, IOException;

     public Map<String, String> getMetadata(final String opType, String container, String object,
     Config config);
    
     public void createMetadata(final String opType, String container, String object, Map<String,
     String> map, Config config);

}
