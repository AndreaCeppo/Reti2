import org.eclipse.paho.client.mqttv3.*;

import java.util.Date;

public class Publisher {

    public static final String BROKER_URL = "tcp://localhost:1883";

    public static final String TOPIC = "PROVA/time";

    private MqttClient client;

    private Date date = new Date();


    public Publisher() {

        //We have to generate a unique Client id.
        String clientId = date.getTime() + "-pub";


        try {

            client = new MqttClient(BROKER_URL, clientId);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void start() {

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill(client.getTopic("PROVA/LWT"), "I'm gone :(".getBytes(), 2, false);

            client.connect(options);

            //Publish data forever
            while (true) {

                publishTime();

                Thread.sleep(100);


            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void publishTime() throws MqttException {
        final MqttTopic timeTopic = client.getTopic(TOPIC);
	date = new Date();
        final String time = "\n" +
                "[color=#FF0000]T[/color][color=#FF8000]h[/color][color=#FFFF00]e[/color][color=#007940] [/color][color=#4040FF]q[/color][color=#A000C0]u[/color][color=#FF0000]i[/color][color=#FF8000]c[/color][color=#FFFF00]k[/color][color=#007940] [/color][color=#4040FF]b[/color][color=#A000C0]r[/color][color=#FF0000]o[/color][color=#FF8000]w[/color][color=#FFFF00]n[/color][color=#007940] [/color][color=#4040FF]f[/color][color=#A000C0]o[/color][color=#FF0000]x[/color][color=#FF8000] [/color][color=#FFFF00]j[/color][color=#007940]u[/color][color=#4040FF]m[/color][color=#A000C0]p[/color][color=#FF0000]s[/color][color=#FF8000] [/color][color=#FFFF00]o[/color][color=#007940]v[/color][color=#4040FF]e[/color][color=#A000C0]r[/color][color=#FF0000] [/color][color=#FF8000]t[/color][color=#FFFF00]h[/color][color=#007940]e[/color][color=#4040FF] [/color][color=#A000C0]l[/color][color=#FF0000]a[/color][color=#FF8000]z[/color][color=#FFFF00]y[/color][color=#007940] [/color][color=#4040FF]d[/color][color=#A000C0]o[/color][color=#FF0000]g[/color]";

        timeTopic.publish(new MqttMessage(time.getBytes()));

    }

    public static void main(String[] args) {
        final Publisher publisher = new Publisher();
        publisher.start();
    }
}
