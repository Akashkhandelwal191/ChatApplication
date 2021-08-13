import java.net.*;

import java.awt.event.*;
import javax.swing.*;

import java.awt.*;
import java.io.*;


public class Client extends JFrame
{
    
    Socket socket;
    BufferedReader br;
    PrintWriter out;
  
    String Name;
    private JLabel heading; //= new JLabel("Person A");
    private JPanel p1 = new JPanel();
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField("Type Something...");
    private JButton b1 = new JButton("Send");
    private Font font = new Font("Roboto",Font.PLAIN,15); 
    String s1= JOptionPane.showInputDialog(this, "Type Your Name for Start the Chat");
 public Client()
 {
      try
      {
        System.out.println("Sending Request to Server");
        socket = new Socket("127.0.0.1",7777);
        System.out.println("Connection done");

         br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          
         out = new PrintWriter(socket.getOutputStream());
         Name = br.readLine();
         out.println(s1);
         out.flush();
         createGUI();
      //   handleEvents();
        startReading();
      //   startWriting();
      

       }
        catch(Exception e)
        {
          e.printStackTrace();
        }
 }

private void createGUI()
{
   
  this.setTitle("Live Chat");
  this.setSize(400,550);
  this.setResizable(false);
  this.setLocationRelativeTo(null);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 
  heading = new JLabel(s1);
  this.getContentPane().setBackground(new Color(221,42,123)); 
  ImageIcon icon1 = new ImageIcon(new ImageIcon("chat.png").getImage().getScaledInstance(60, 30, Image.SCALE_DEFAULT));
  heading.setIcon(icon1);
  
  //ImageIcon icon = new ImageIcon(new ImageIcon("DP5.png").getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT));
  //heading.setIcon(icon);
  heading.setHorizontalAlignment(SwingConstants.LEFT);
  heading.setAlignmentX(0);

  
  heading.setBackground(new Color(221,42,123));
  heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
  heading.setFont(font);
  heading.setForeground(Color.BLACK);

  messageArea.setFont(font);
  messageArea.setBackground(Color.BLACK);
  messageArea.setForeground(Color.WHITE);
  messageArea.setEditable(false);
  messageInput.setFont(font);
  
  this.setLayout(new BorderLayout());
  
  this.add(heading,BorderLayout.NORTH);
  JScrollPane jScrollpane = new JScrollPane(messageArea); 
  this.add(jScrollpane,BorderLayout.CENTER);
  //this.add(messageArea,BorderLayout.CENTER);
  this.add(messageInput,BorderLayout.SOUTH); 
  //this.add(b1,BorderLayout.WEST);
  //this.add(p1,BorderLayout.WEST);
  //p1.setBounds(0, 50, 30, 200);
  p1.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
  p1.setBackground(Color.DARK_GRAY);
  this.add(p1,BorderLayout.WEST);
  b1.setBackground(Color.BLACK);
  b1.setForeground(Color.WHITE);
  p1.add(b1);
 // messageInput.add(b1,BorderLayout.SOUTH);
  this.setVisible(true);
  b1.addActionListener(new A1());

}
class A1 implements ActionListener
{
     public void actionPerformed(ActionEvent e)
     {
         String contenttoSend = messageInput.getText();
         //messageArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
         messageArea.append("Me :"+contenttoSend+"\n");
         out.println(contenttoSend);
         out.flush();
         messageInput.setText("Type Something....");    
         messageInput.requestFocus();
         if(contenttoSend.equals("exit"))
              {
                  
                  JOptionPane.showMessageDialog(null,"You Terminated the Chat");
                  messageInput.setEnabled(false);
                  b1.setEnabled(false);
                  try
                  {
                  socket.close();
                  }
                  catch(Exception ae)
                  {
                    ae.printStackTrace();
                  }
              }
        
     }
}
public void startReading()
{ 
         Runnable r1=()->{
           System.out.println("Reader Started"); 
        try
           {             
           while(!socket.isClosed())
           {
              String msg = br.readLine();
              if(msg.equals("exit"))
              {
                  //System.out.println("Server Terminated the Chat");
                  JOptionPane.showMessageDialog(this, Name+"Terminated the Chat");
                  messageInput.setEnabled(false);
                  b1.setEnabled(false);
                  socket.close();
                  break;
              }
             // System.out.println("Server :"+msg);
            
             messageArea.append(Name+":"+msg+"\n");
           }        
        }
          catch(Exception e)
        { 
              e.printStackTrace();
        }
    };
         new Thread(r1).start();
        
}
     public void startWriting()
     {
        Runnable r2=()->
        {     System.out.println("Writer Started:");

        while(!socket.isClosed())
              {
                  try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();

                  } catch (Exception e) {
                      e.printStackTrace();
                  }
     

              }

        };
         new Thread(r2).start(); 
     }
       public static void main(String[] args) {
            System.out.println("this is Client:");
            new Client();
        } 
}
