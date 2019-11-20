import org.eclipse.paho.client.mqttv3.*;

import java.util.Scanner;

public class Chat {


    public static final String BROKER_URL = "tcp://193.206.55.23:1883";
    public static final String BASE_TOPIC = "chat/";

    private Scanner scanner;
    private String localUsername;
    private String remoteUsername;

    private MqttClient client;

    public Chat(){
        this.scanner = new Scanner(System.in);
    }



    private void start() {
        System.out.println("Insert your username: ");
        this.localUsername = scanner.nextLine();
        System.out.println("Insert remote username: ");
        this.remoteUsername = scanner.nextLine();

        try {
            client = new MqttClient(BROKER_URL, localUsername);
        } catch (MqttException e) {
            e.printStackTrace();
            return;
        }

        String fromChannel = BASE_TOPIC + remoteUsername + "_" + localUsername;
        String toChannel = BASE_TOPIC + localUsername + "_" + remoteUsername;

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setWill(client.getTopic(toChannel), "I'm gone :(".getBytes(), 0, false);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(remoteUsername + " > " + mqttMessage.toString());

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        try {
             client.connect(options);
            client.subscribe(fromChannel);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        while (true){
            String message = scanner.nextLine();
            if(message.equals("q"))
                System.exit(0);

            final MqttTopic channelTopic = client.getTopic(toChannel);
            try {
                channelTopic.publish(new MqttMessage(message.getBytes()));
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Chat chat = new Chat();
        chat.start();

    }
}
