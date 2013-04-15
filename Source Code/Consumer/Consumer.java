import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import org.jfree.data.time.Millisecond;


public class Consumer extends Thread{
	
	private MonitoringGUI myGUI;
	private String url = "tcp://129.79.49.248:61616";
	private static String subject = "DS_G14_P4";
	
	MessageConsumer consumer;
	int node  = 0;
	public Consumer(MonitoringGUI gui){

		myGUI = gui;
		
		Connection connection = null;
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			
						
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        Destination destination = session.createTopic(subject);
	        
	        consumer = session.createConsumer(destination);

			} catch (JMSException e) {
				e.printStackTrace();
			}
		
}
	
	public void run(){
		while(true){
			String text = null;
			try {
				TextMessage message = (TextMessage) consumer.receive();
				text = message.getText();
				// System.out.println("received "+text);
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
			
			String[] strings = text.split(" ");
		
			//if((System.currentTimeMillis()- Long.parseLong(strings[5])) > 2000)
			//	continue;
		
			node = Integer.parseInt(strings[0]);
			double procCpuValue = Double.parseDouble(strings[1]);
			double procMemValue = Double.parseDouble(strings[2]);
			double cpuValue = Double.parseDouble(strings[3]);
			double memValue = Double.parseDouble(strings[4]);
			long recordTime = Long.parseLong(strings[5]);
			System.out.println("Received");
			System.out.println("node:"+node+" procCpu:"+procCpuValue+" procmem:"+procMemValue+" cpuValue:"+cpuValue+" memValue:"+memValue+" recordTime:"+recordTime);
			myGUI.addNewPoints(node-1, procCpuValue, procMemValue, cpuValue, memValue, recordTime);
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}

}
