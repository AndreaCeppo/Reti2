
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class Benchmark
{

	private final String brokerUrl = "tcp://localhost:1883";

	private final int nSubs4Topic = 5;
	private final int nPubs4Topic = 5;
	private final int nTopics = 5;
	private final int nMessages4Pub = 1000;
	private final int qos = 2;

	public Benchmark()
	{


	}

	public synchronized void start() throws InterruptedException, MqttException
	{
		ArrayList<StressSubscriber> stressSubscribers = new ArrayList<>();
		//StressSubscriber baseStressSubscriber = new StressSubscriber(brokerUrl, "#");
		//stressSubscribers.add(baseStressSubscriber);
		//Thread baseSubThread = new Thread(baseStressSubscriber);
		//baseSubThread.start();
		String[] topics = new String[nTopics];
		for (int i = 0; i < nTopics; i++)
		{
			topics[i] = UUID.randomUUID().toString();
			for (int j = 0; j < nSubs4Topic; j++)
			{
				StressSubscriber stressSubscriber = new StressSubscriber(brokerUrl, topics[i]);
				stressSubscribers.add(stressSubscriber);
				Thread subThread = new Thread(stressSubscriber);
				subThread.start();
			}
		}

		for (int i = 0; i < nTopics; i++)
		{
			for (int j = 0; j < nPubs4Topic; j++)
			{
				StressPublisher stressPublisher = new StressPublisher(brokerUrl, topics[i], qos, nMessages4Pub, this);
				Thread pubThread = new Thread(stressPublisher);
				pubThread.start();
			}
		}
		for (int i = 0; i < nPubs4Topic; i++)
		{
			this.wait();
		}
		Thread.sleep(60 * 3 * 1000);
		float sum = 0;
		long  count = 0;
		for (StressSubscriber stressSubscriber : stressSubscribers)
		{
			stressSubscriber.disconnect();

			long tempSum = 0;
			long tempCount = 0;
			for (BenchmarkData benchmarkData : stressSubscriber.getList())
			{
				sum += (benchmarkData.getEndDate() - benchmarkData.getStartDate());
				tempSum += (benchmarkData.getEndDate() - benchmarkData.getStartDate());
				count++;
				tempCount++;
			}
			if (nSubs4Topic * nMessages4Pub != tempCount)
			{
				System.out.println("--------------------------");
				System.out.println("Topic: " + stressSubscriber.getTopic());
				System.out.println("TempCountAttended: " + nSubs4Topic * nMessages4Pub);
				System.out.println("TempCount: " + tempCount);
				System.out.println("TempAvg: " + (tempSum / tempCount));
			}

		}

		System.out.println("QOS: " + qos);
		System.out.println("nTopics: " + nTopics);
		System.out.println("nSubs4Topic: " + nSubs4Topic);
		System.out.println("nPubs4Topic: " + nPubs4Topic);
		System.out.println("nMessages4Pub: " + nMessages4Pub);
		System.out.println("totVolume: " + nTopics * nPubs4Topic * nMessages4Pub);
		System.out.println("expectedArrived: " + (nTopics * nPubs4Topic * nMessages4Pub * nSubs4Topic)); //* 2 + nTopics * nPubs4Topic * nMessages4Pub * (nSubs4Topic - 1)));

		System.out.println("totArrived: " + count);
		System.out.println("timeAVG: " + (sum / count));



	}


	public static void main(String[] args) throws InterruptedException, MqttException
	{
		Benchmark benchmark = new Benchmark();
		benchmark.start();
	}

}
