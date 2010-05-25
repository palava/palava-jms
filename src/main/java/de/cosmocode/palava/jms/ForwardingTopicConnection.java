package de.cosmocode.palava.jms;

import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

import com.google.common.collect.ForwardingObject;

/**
 * Abstract {@link TopicConnection} decorator.
 *
 * @since 1.0
 * @author Willi Schoenborn
 */
public abstract class ForwardingTopicConnection extends ForwardingObject implements TopicConnection {

    @Override
    protected abstract TopicConnection delegate();
    
    @Override
    public void close() throws JMSException {
        delegate().close();
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector,
        ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return delegate().createConnectionConsumer(destination, messageSelector, sessionPool, maxMessages);
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Topic topic, String messageSelector,
        ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return delegate().createConnectionConsumer(topic, messageSelector, sessionPool, maxMessages);
    }

    @Override
    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName,
        String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return delegate().createDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool,
            maxMessages);
    }

    @Override
    public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
        return delegate().createSession(transacted, acknowledgeMode);
    }

    @Override
    public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException {
        return delegate().createTopicSession(transacted, acknowledgeMode);
    }

    @Override
    public String getClientID() throws JMSException {
        return delegate().getClientID();
    }

    @Override
    public ExceptionListener getExceptionListener() throws JMSException {
        return delegate().getExceptionListener();
    }

    @Override
    public ConnectionMetaData getMetaData() throws JMSException {
        return delegate().getMetaData();
    }

    @Override
    public void setClientID(String clientID) throws JMSException {
        delegate().setClientID(clientID);
    }

    @Override
    public void setExceptionListener(ExceptionListener listener) throws JMSException {
        delegate().setExceptionListener(listener);
    }

    @Override
    public void start() throws JMSException {
        delegate().start();
    }

    @Override
    public void stop() throws JMSException {
        delegate().stop();
    }
    
}
