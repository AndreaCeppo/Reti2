import org.eclipse.paho.client.mqttv3.*;

import java.util.*;

public class Benchmark
{
	public static final String BROKER_URL = "tcp://193.206.55.23:1883";

	public static final String TOPIC = "infouniupo/benchmark";

	public static final int QTA = 10000;

	private MqttClient client;



	public Benchmark() {


	}

	private void startTest(int qos, int qta, int sleep) {

		try {
			String clientId = "infouniupo-benchmark";
			client = new MqttClient(BROKER_URL, clientId);

			final MqttTopic timeTopic = client.getTopic(TOPIC);

			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setWill(client.getTopic(TOPIC), "0_0_0".getBytes(), qos, false);


			client.connect(options);

			List<String> arrivedData = Collections.synchronizedList(new ArrayList<String>());

			//register callback
			client.setCallback(new MqttCallback()
			{
				@Override
				public void connectionLost(Throwable throwable)
				{
				}

				@Override
				public void messageArrived(String s, MqttMessage mqttMessage) throws Exception
				{
					arrivedData.add(new java.lang.String(mqttMessage.getPayload())+"_"+new Date().getTime());
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
				{
				}
			});
			client.subscribe(TOPIC);



			for(int i = 0; i<qta; i++){
				timeTopic.publish(new MqttMessage((new Date().getTime() + "_" + i ).getBytes()));
				Thread.sleep(sleep);
			}

			client.disconnect();
			client.close();

			Thread.sleep(1000);
			createReport(qos,qta,sleep,arrivedData);


		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void createReport(int qos, int qta, int sleep, List<String> arrivedData)
	{
		synchronized(arrivedData)
		{
			int maxSeq = 0;
			int missOrder = 0;
			int timeSum = 0;
			long startMissOrder = 0;
			long endTime = 0;
			for(String mqttMessage: arrivedData){
				String[] messageData = mqttMessage.split("_");
				long t0 = Long.parseLong(messageData[0]);
				long t1 = Long.parseLong(messageData[2]);
				int seq = Integer.parseInt(messageData[1]);
				if(endTime < t1)
					endTime = t1;
				timeSum += t1 - t0;
				if(maxSeq == seq)
					maxSeq ++;
				else if(maxSeq < seq){
					maxSeq = seq;
					missOrder ++;
					if(startMissOrder == 0)
						startMissOrder = t1;
				}
			}
			System.out.println("QOS:\t\t" + qos);
			System.out.println("QTA:\t\t" + qta);
			System.out.println("SLEEP:\t\t" + sleep);
			System.out.println("AVG:\t\t" + timeSum / (float) arrivedData.size());
			System.out.println("MISS ORDER:\t\t" + missOrder);
			System.out.println("FIRST MISS ORDER:\t\t" + (endTime - startMissOrder));
			System.out.println("QTA - ARRIVED:\t\t" + (qta - arrivedData.size()));
			System.out.println();

		}
	}

	public void test() throws MqttException
	{
		//startTest(0,100,10);
		startTest(0,2000,10);
		//startTest(0,10000,10);
		//startTest(1,100,10);
		startTest(1,2000,10);
		//startTest(1,10000,10);
		//startTest(2,100,10);
		startTest(2,2000,10);
		//startTest(2,10000,10);


	}



	public static void main(String[] args) throws MqttException
	{

		new Benchmark().test();


	}
}
