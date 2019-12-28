

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;


public class StressPublisher implements Runnable
{

	private final String topic;
	private final String clientId;
	private MqttClient mqttClient;
	private final int qos;
	private final int totMsg;
	private final Object monitor;

	public StressPublisher(String brokerUrl, String topic, int qos, int totMsg, Object monitor)
	{
		this.topic = topic;
		this.qos = qos;
		this.totMsg = totMsg;
		this.monitor = monitor;
		this.clientId = UUID.randomUUID() + "_pub";
		try
		{
			MqttDefaultFilePersistence mqttPersistence = new MqttDefaultFilePersistence("./lock");
			mqttClient = new MqttClient(brokerUrl, clientId, mqttPersistence);


		} catch (MqttException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run()
	{
		try
		{
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);

			final MqttTopic timeTopic = mqttClient.getTopic(topic);
			options.setWill(topic, longToBytes(0), qos, false);
			options.setMaxInflight(1000000);

			mqttClient.connect(options);

			//Publish data forever
			for (int i = 0; i < totMsg; i++)
			{


				timeTopic.publish(new MqttMessage(longToBytes(new Date().getTime())));
			}

			mqttClient.disconnect();
			mqttClient.close();
			synchronized (monitor)
			{
				monitor.notify();
			}
		} catch (MqttException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public byte[] longToBytes(long x)
	{
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(x);
		return buffer.array();
	}

	public static void main(String[] args)
	{
		StressPublisher stressPublisher = new StressPublisher("tcp://localhost:1883", "/", 0, 10, new Object());
		Thread pubThread = new Thread(stressPublisher);
		pubThread.start();

	}

}
