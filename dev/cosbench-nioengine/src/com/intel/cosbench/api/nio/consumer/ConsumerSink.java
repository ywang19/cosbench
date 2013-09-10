package com.intel.cosbench.api.nio.consumer;

import java.io.IOException;

//import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentDecoder;

/**
 * The class abstracts the functionalities necessary for one data consumer.
 * 
 * @author ywang19
 *
 * @param <T>
 */
abstract class ConsumerSink<T> {
//    protected HttpResponse response;
    protected ContentType contentType;
    protected T sink;
    
    abstract long getLength();
    
	abstract void disconnect() throws IOException;
	
	abstract void close();
	
	abstract void consume(ContentDecoder decoder) throws IOException;
	
	abstract void connect(ContentType contentType) throws IOException;
	
	public ConsumerSink(T sink) {
        if (sink == null) {
            throw new IllegalArgumentException("File may nor be null");
        }
        this.sink = sink;
	}
	
	ContentType getContentType() {
		return this.contentType; 
	}
	
	T getSink() {
		return this.sink;
	}
}
