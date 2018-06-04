package br.com.orion.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hornetq.api.core.management.ObjectNameBuilder;
import org.hornetq.api.jms.management.JMSQueueControl;

import com.google.common.collect.Lists;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JmsUtils {

	private JmsUtils() {
		super();
	}

	public static <T extends Serializable> void sendMessage(String queueName, T message) {
		Collection<T> lista = new ArrayList<>();
		lista.add(message);

		sendMessages(queueName, lista, false, false, DeliveryMode.PERSISTENT);
	}
	
	public static <T extends Serializable> void sendMessage(String queueName, T message, int deliveryMode) {
		Collection<T> lista = new ArrayList<>();
		lista.add(message);

		sendMessages(queueName, lista, true, true, deliveryMode);
	}


	public static void sendMessages(String queueName, Collection<? extends Serializable> messages, boolean disableMessageID, boolean disableMessageTimestamp,
			int deliveryMode) {
		ConnectionFactory connectionFactory = createConectionFactory();

		try (JMSContext jmsContext = connectionFactory.createContext()) {

			Queue queue = jmsContext.createQueue(queueName);

			JMSProducer producer = jmsContext.createProducer();
			producer.setDisableMessageID(disableMessageID);
			producer.setDisableMessageTimestamp(disableMessageTimestamp);
			producer.setDeliveryMode(deliveryMode);

			for (Serializable message : messages) {
				producer.send(queue, message);
			}
		}

	}

	public static <T extends Serializable> void sendMessages(String queueName, List<T> messages,
			Integer partitionSize) {
		Collection<ArrayList<T>> tempList = new ArrayList<>();

		List<List<T>> listPartitioned = Lists.partition(messages, partitionSize);
		for (List<T> list : listPartitioned) {
			tempList.add(new ArrayList<>(list));
		}

		sendMessages(queueName, tempList, false, false, DeliveryMode.PERSISTENT);
	}

	@SneakyThrows(JMSException.class)
	public static void removeMessages(String queueName) {
		ConnectionFactory connectionFactory = createConectionFactory();

		try (JMSContext jmsContext = connectionFactory.createContext()) {
			Queue queue = jmsContext.createQueue(queueName);

			try (QueueBrowser queueBrowser = jmsContext.createBrowser(queue)) {
				Enumeration<?> enumeration = queueBrowser.getEnumeration();

				while (enumeration.hasMoreElements()) {
					enumeration.nextElement();
				}
			}
		}
	}


	@SneakyThrows(Exception.class)
	public static boolean hasMoreMessages(String queueName) {
		boolean hasMoreMessage = false;
		ConnectionFactory connectionFactory = createConectionFactory();

		try (JMSContext jmsContext = connectionFactory.createContext()) {
			Queue queue = jmsContext.createQueue(queueName);

			ObjectName objectName = ObjectNameBuilder.DEFAULT.getJMSQueueObjectName(queue.getQueueName());

			try (JMXConnector connector = JMXConnectorFactory.connect(
					new JMXServiceURL("service:jmx:http-remoting-jmx://localhost:9990"),
					new HashMap<String, Object>())) {
				MBeanServerConnection mbsc = connector.getMBeanServerConnection();
				JMSQueueControl queueControl = MBeanServerInvocationHandler.newProxyInstance(mbsc, objectName,
						JMSQueueControl.class, false);
				
				log.info("STATUS DA FILA:");
				log.info("Mensagens na fila prontas para processamento    : " + queueControl.getMessageCount());
				log.info("Mensagens agendadas para fila de processamento: : " + queueControl.getScheduledCount());
				
				if ((queueControl.getMessageCount() + queueControl.getScheduledCount()) > 0) {
					hasMoreMessage = true;
				}
			}
		}

		return hasMoreMessage;
	}

	@SneakyThrows(NamingException.class)
	private static ConnectionFactory createConectionFactory() {
		javax.naming.Context context;
		context = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		return connectionFactory;
	}
	
}