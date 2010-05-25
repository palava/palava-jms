package de.cosmocode.palava.jms;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Default {@link TopicService} implementation.
 *
 * @since 1.0
 * @author Willi Schoenborn
 */
final class DefaultTopicService implements TopicService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTopicService.class);

    private final Provider<TopicConnection> provider;
    
    @Inject
    public DefaultTopicService(Provider<TopicConnection> provider) {
        this.provider = Preconditions.checkNotNull(provider, "Provider");
    }
    
    @Override
    public void subscribe(Topic topic, Iterable<MessageListener> listeners) throws JMSException {
        final TopicConnection connection = provider.get();
        final TopicSession session = connection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);
        
        for (MessageListener listener : listeners) {
            LOG.debug("Registering {} for topic {}", listener, topic);
            session.createSubscriber(topic).setMessageListener(listener);
        }
        
        connection.start();
    }
    
}
