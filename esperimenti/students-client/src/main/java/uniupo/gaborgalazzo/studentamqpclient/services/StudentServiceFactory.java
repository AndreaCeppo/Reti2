package uniupo.gaborgalazzo.studentamqpclient.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceFactory
{
	@Autowired
	private RestStudentService restStudentService;
	@Autowired
	private AmqpServerStudentService amqpServerStudentService;
	@Autowired
	private AmqpProxyStudentService amqpProxyStudentService;
	@Autowired
	private MqttProxyStudentService mqttProxyStudentService;

	public RestStudentService getRestStudentService()
	{
		return restStudentService;
	}

	public AmqpServerStudentService getAmqpServerStudentService()
	{
		return amqpServerStudentService;
	}

	public AmqpProxyStudentService getAmqpProxyStudentService()
	{
		return amqpProxyStudentService;
	}

	public MqttProxyStudentService getMqttProxyStudentService()
	{
		return mqttProxyStudentService;
	}
}
