package benchmark;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.ByteBuffer;

public class BenchmarkData
{
	private MqttMessage message;
	private long startDate;
	private long endDate;

	public BenchmarkData(long endDate, MqttMessage message)
	{
		this.endDate = endDate;
		this.message = message;
	}

	public long getStartDate()
	{
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(message.getPayload());
		buffer.flip();//need flip
		return buffer.getLong();
	}

	public void setStartDate(long startDate)
	{
		this.startDate = startDate;
	}

	public long getEndDate()
	{
		return endDate;
	}

	public void setEndDate(long endDate)
	{
		this.endDate = endDate;
	}

	public MqttMessage getMessage()
	{

		return message;
	}

	public void setMessage(MqttMessage message)
	{
		this.message = message;
	}



}
