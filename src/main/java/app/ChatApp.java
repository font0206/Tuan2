package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.BasicConfigurator;



public class ChatApp extends JFrame implements ActionListener {

	JPanel panel;
    JTextField textField;
    JTextArea textArea;
    JButton button;
	
	public ChatApp() {
		
        panel = new JPanel();
        textField = new JTextField();
        textArea = new JTextArea();
        button = new JButton("Send");
        this.setSize(500, 500);
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.setLayout(null);
        this.add(panel);
        textArea.setBounds(20, 20, 450, 360);
        panel.add(textArea);
        textField.setBounds(20, 400, 340, 30);
        panel.add(textField);
        button.setBounds(375, 400, 95, 30);
        panel.add(button);
        button.addActionListener(this);

	}
	

	public static void main(String[] args) {
		new ChatApp();

	}

	public void actionPerformed(ActionEvent e) {
		
		try {
			//config environment for JMS
			BasicConfigurator.configure();
	//config environment for JNDI
			Properties settings = new Properties();
			settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
	//create context
			Context ctx = new InitialContext(settings);
	//lookup JMS connection factory
			ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
	//lookup destination. (If not exist-->ActiveMQ create once)
			Destination destination = (Destination) ctx.lookup("dynamicQueues/thanthidet");
	//get connection using credential
			Connection con = factory.createConnection("admin", "admin");
	//connect to MOM
			con.start();
	//create session
			Session session = con.createSession(/* transaction */false, /* ACK */Session.AUTO_ACKNOWLEDGE);
	//create producer
			MessageProducer producer = session.createProducer(destination);
	//create text message
			Message msg = session.createTextMessage("hello mesage from ActiveMQ");
			producer.send(msg);
			String mess = textArea.getText().toString().trim();
			
			msg = session.createTextMessage(mess);
			producer.send(msg);
	//shutdown connection
			session.close();
			con.close();
			System.out.println("Finished...");
		
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
		
		
	}

}
