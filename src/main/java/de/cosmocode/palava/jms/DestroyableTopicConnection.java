package de.cosmocode.palava.jms;

import javax.jms.JMSException;
import javax.jms.TopicConnection;

import com.google.common.base.Preconditions;

import de.cosmocode.palava.scope.Destroyable;

/**
 * {@link Destroyable} {@link TopicConnection} implementation.
 *
 * @since 1.0
 * @author Willi Schoenborn
 */
public final class DestroyableTopicConnection extends ForwardingTopicConnection implements Destroyable {

    private final TopicConnection connection;
    
    public DestroyableTopicConnection(TopicConnection connection) {
        this.connection = Preconditions.checkNotNull(connection, "Connection"); 
    }
    
    @Override
    protected TopicConnection delegate() {
        return connection;
    }

    @Override
    public void destroy() {
        try {
            close();
        } catch (JMSException e) {
            throw new IllegalStateException(e);
        }
    }
    
}
