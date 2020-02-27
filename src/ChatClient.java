import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    static JFrame chatWindow = new JFrame("Chat Application");
    static JTextArea chatArea = new JTextArea(22,40);
    static JTextField textField = new JTextField(40);
    static JLabel blankLabel = new JLabel("             ");
    static JButton sendButton = new JButton("Send");
    static BufferedReader in;
    static PrintWriter out;
    static JLabel nameLabel = new JLabel("    ");

    ChatClient(){

        chatWindow.setLayout(new FlowLayout());

        chatWindow.add(nameLabel);
        chatWindow.add(new JScrollPane(chatArea));
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendButton);

        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(475, 500);
        chatWindow.setVisible(true);

        textField.setEditable(false);
        chatArea.setEditable(false);

        sendButton.addActionListener(new Listener());
        textField.addActionListener(new Listener()); // sende besked med enter
    }

    void startChat() throws Exception {
        // main logic for ChatClient
        //ask user to input ip adress.

        String ipAddress = JOptionPane.showInputDialog( // showInputDialog metode, det som brugeren inputter bliver til ipAddress
                chatWindow, //hvor det vises
        "Enter IP Address:", // beskeden der vises
        "Ip Address Required!", //titlen på dialog boksen
        JOptionPane.PLAIN_MESSAGE); //Type af beskeden

        Socket soc = new Socket(ipAddress, 9806);
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out = new PrintWriter(soc.getOutputStream(), true);

        while (true){
            String str = in.readLine(); // recieve namerequired action
            if (str.equals("NAMEREQUIRED")){
                String name = JOptionPane.showInputDialog(
                        chatWindow,
                        "Enter a unique name:",
                        "Name Required!",
                        JOptionPane.PLAIN_MESSAGE);

                out.println(name);// input fra bruger sendes til server

            }
            else if(str.equals("NAMEALREDYEXISTS")){
                String name = JOptionPane.showInputDialog(
                        chatWindow,
                        "Enter another name:",
                        "Name already exists!",
                        JOptionPane.WARNING_MESSAGE);

                out.println(name); // input fra brugeren sendes til server
            }
            else if(str.equals("NAMEACCEPTED")){
                textField.setEditable(true);
                nameLabel.setText("You are logged in as:" + str.substring(12));
            }
            else{
                chatArea.append(str + "\n" );
            }
        }
    }

    public static void main(String[] args) throws Exception{

        ChatClient client = new ChatClient();
        client.startChat();

    }
}

class Listener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e){
        ChatClient.out.println(ChatClient.textField.getText()); // sender text fra client til server
        ChatClient.textField.setText("");
    }
}
