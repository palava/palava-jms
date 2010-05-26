/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cosmocode.palava.jms;

import java.lang.annotation.Annotation;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import de.cosmocode.palava.core.inject.AbstractRebindModule;
import de.cosmocode.palava.core.inject.Config;
import de.cosmocode.palava.core.inject.RebindModule;

/**
 * Binds {@link QueueConnectionFactory}, {@link QueueConnection}, etc using
 * the specified annotation.
 *
 * @since 1.0
 * @author Willi Schoenborn
 */
public final class JmsModule extends AbstractRebindModule {

    private final Class<? extends Annotation> annotation;
    
    private final Config config;
    
    @Inject
    private JmsModule(Class<? extends Annotation> annotation, String prefix) {
        this.annotation = Preconditions.checkNotNull(annotation, "Annotation");
        this.config = new Config(prefix);
    }

    @Override
    protected void configuration() {
        bind(String.class).annotatedWith(Names.named(JmsConfig.JNDI_NAME)).to(
            Key.get(String.class, Names.named(config.prefixed(JmsConfig.JNDI_NAME))));
    }
    
    @Override
    protected void optionals() {
        // nothing optional
    }
    
    @Override
    protected void bindings() {
        bind(QueueConnectionFactory.class).annotatedWith(annotation).
            toProvider(QueueConnectionFactoryProvider.class).in(Singleton.class);
        
        bind(QueueConnection.class).annotatedWith(annotation).
            toProvider(QueueConnectionProvider.class);
        
        bind(TopicConnectionFactory.class).annotatedWith(annotation).
            toProvider(TopicConnectionFactoryProvider.class).in(Singleton.class);
        
        bind(TopicConnection.class).annotatedWith(annotation).
            toProvider(TopicConnectionProvider.class);
    }

    @Override
    protected void expose() {
        expose(QueueConnectionFactory.class).annotatedWith(annotation);
        expose(QueueConnection.class).annotatedWith(annotation);
        expose(TopicConnectionFactory.class).annotatedWith(annotation);
        expose(TopicConnection.class).annotatedWith(annotation);
    }

    /**
     * Internal jndi based {@link Provider} implementation for {@link QueueConnectionFactory}s.
     *
     * @since 1.0
     * @author Willi Schoenborn
     */
    static final class QueueConnectionFactoryProvider implements Provider<QueueConnectionFactory> {

        private final Context context;
        
        private String name = "jms/QueueConnectionFactory";
        
        @Inject
        public QueueConnectionFactoryProvider(Context context) {
            this.context = Preconditions.checkNotNull(context, "Context");
        }
        
        @Inject(optional = true)
        void setName(@Named(JmsConfig.JNDI_NAME) String name) {
            this.name = Preconditions.checkNotNull(name, "Name");
        }
        
        @Override
        public QueueConnectionFactory get() {
            try {
                return QueueConnectionFactory.class.cast(context.lookup(name));
            } catch (NamingException e) {
                throw new IllegalStateException(e);
            }
        };
        
    }
    
    /**
     * Internal {@link Provider} implementation for {@link QueueConnection}s.
     *
     * @since 1.0
     * @author Willi Schoenborn
     */
    static final class QueueConnectionProvider implements Provider<QueueConnection> {
        
        private final QueueConnectionFactory factory;

        @Inject
        public QueueConnectionProvider(QueueConnectionFactory factory) {
            this.factory = Preconditions.checkNotNull(factory, "Factory");
        }
        
        @Override
        public QueueConnection get() {
            try {
                return factory.createQueueConnection();
            } catch (JMSException e) {
                throw new IllegalStateException(e);
            }
        }
        
    }

    /**
     * Internal jndi based {@link Provider} implementation for {@link TopicConnectionFactory}s.
     *
     * @since 1.0
     * @author Willi Schoenborn
     */
    static final class TopicConnectionFactoryProvider implements Provider<TopicConnectionFactory> {

        private final Context context;
        
        private String name = "jms/TopicConnectionFactory";
        
        @Inject
        public TopicConnectionFactoryProvider(Context context) {
            this.context = Preconditions.checkNotNull(context, "Context");
        }
        
        @Inject(optional = true)
        void setName(@Named(JmsConfig.JNDI_NAME) String name) {
            this.name = Preconditions.checkNotNull(name, "Name");
        }
        
        @Override
        public TopicConnectionFactory get() {
            try {
                return TopicConnectionFactory.class.cast(context.lookup(name));
            } catch (NamingException e) {
                throw new IllegalStateException(e);
            }
        };
        
    }
    
    /**
     * Internal {@link Provider} implementation for {@link TopicConnection}s.
     *
     * @since 1.0
     * @author Willi Schoenborn
     */
    static final class TopicConnectionProvider implements Provider<TopicConnection> {
        
        private final TopicConnectionFactory factory;

        @Inject
        public TopicConnectionProvider(TopicConnectionFactory factory) {
            this.factory = Preconditions.checkNotNull(factory, "Factory");
        }
        
        @Override
        public TopicConnection get() {
            try {
                return factory.createTopicConnection();
            } catch (JMSException e) {
                throw new IllegalStateException(e);
            }
        }
        
    }
    
    /**
     * Creates a rebinding module which uses the specified annotation and prefix
     * to rebind jms bindings to annotated ones.
     * 
     * @since 1.0
     * @param annotation the binding annotation
     * @param prefix the config prefix
     * @return rebinding module
     */
    public static RebindModule annotatedWith(Class<? extends Annotation> annotation, String prefix) {
        return new JmsModule(annotation, prefix);
    }
    
}
