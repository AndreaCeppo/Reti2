

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.SynchronousQueue;


public class StressSubscriber implements Runnable
{

	private final String topic;
	private final String clientId;
	private MqttClient mqttClient;
	private List<BenchmarkData> list =
			Collections.synchronizedList(new ArrayList<BenchmarkData>());

	public StressSubscriber(String brokerUrl, String topic) {
		this.topic = topic;
		this.clientId = UUID.randomUUID() + "_sub";
		try {
			MqttDefaultFilePersistence mqttPersistence = new MqttDefaultFilePersistence("./lock");
			mqttClient = new MqttClient(brokerUrl, clientId, mqttPersistence);


		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run() {

		try {

			mqttClient.setCallback(new MqttCallbackExtended()
			{
				@Override
				public void connectComplete(boolean b, String s)
				{
				}

				@Override
				public void connectionLost(Throwable throwable)
				{
				}

				@Override
				public synchronized void messageArrived(String s, MqttMessage mqttMessage) throws Exception
				{
					BenchmarkData benchmarkData = new BenchmarkData(new Date().getTime(), mqttMessage);
					list.add(benchmarkData);
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
				{
				}
			});
			mqttClient.connect();

			//Subscribe to all subtopics of home
			mqttClient.subscribe(topic);


		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void disconnect() throws MqttException
	{
		mqttClient.disconnect();
		mqttClient.close();
	}


	public List<BenchmarkData> getList()
	{
		return list;
	}

	public static void main(String[] args)
	{
		StressSubscriber stressSubscriber = new StressSubscriber("tcp://localhost:1883", "#");
		Thread subThread = new Thread(stressSubscriber);
		subThread.start();

		//subThread.wait();
	}

	public String getTopic()
	{
		return topic;
	}

	public String getClientId()
	{
		return clientId;
	}
}
