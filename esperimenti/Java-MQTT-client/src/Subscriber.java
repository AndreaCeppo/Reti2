import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.util.Date;

public class Subscriber {

    public static final String BROKER_URL = "tcp://localhost:1883";
    private Date date = new Date();

    public static final String TOPIC = "#";
    //We have to generate a unique Client id.
    String clientId = date.getTime() + "-sub";
    private MqttClient mqttClient;

    public Subscriber() {

        try {
            mqttClient = new MqttClient(BROKER_URL, clientId);


        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() {
        try {

            mqttClient.setCallback(new SubscribeCallback());
            mqttClient.connect();

            //Subscribe to all subtopics of home
            final String topic = TOPIC;
            mqttClient.subscribe(topic);

            System.out.println("src.Subscriber is now listening to "+topic);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        final Subscriber subscriber = new Subscriber();
        subscriber.start();
    }

}
