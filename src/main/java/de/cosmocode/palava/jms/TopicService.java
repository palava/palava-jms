package de.cosmocode.palava.jms;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Topic;

/**
 * 
 *
 * @since 
 * @author Willi Schoenborn
 */
public interface TopicService  {

    void subscribe(Topic topic, Iterable<MessageListener> listeners) throws JMSException;
    
}
