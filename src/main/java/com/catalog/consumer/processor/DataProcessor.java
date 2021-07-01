package com.catalog.consumer.processor;

/**
 * Interface to manipulate data and execute processing.
 *
 *
 * @param <T>
 * @param <V>
 */
public interface DataProcessor<T,V> {

    /**
     * A Target to be validated and updated by executing processes based on the data provided.
     *
     * @param target Target source to be manipulated.
     * @param data Any information that helps to decide on how to Update the target source.
     */
    void processData(T target, V data);

}