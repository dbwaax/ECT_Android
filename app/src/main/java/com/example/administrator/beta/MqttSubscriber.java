package com.example.administrator.beta;

import android.widget.Toast;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class MqttSubscriber {
    public static final String  MQTT_SERVER_URL="47.95.238.162";
    private static  String buff = null;
    public  MqttSubscriber(){

    }
    public static void listening() throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost(MQTT_SERVER_URL, 1883);
        BlockingConnection connection = mqtt.blockingConnection();
        connection.connect();
        Topic[] topics = {new Topic("test", QoS.AT_LEAST_ONCE)};
        byte[] qoses = connection.subscribe(topics);
        Message message = connection.receive();
        System.out.println(message.getTopic());
        byte[] payload = message.getPayload();
        buff = new String(payload);
        System.out.println("打印接收到的消息："+new String(payload));
        message.ack();
        connection.disconnect();
    }
    public String getBuff(){
        return buff;
    }
}
