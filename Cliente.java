import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente implements Runnable {
  static PrintStream os = null;
  static boolean paraThread = false;
  String ip = "107.0.0.1";

  public void run () {
    Socket socket = null;
    Scanner is = null;

    try {
      socket = new Socket("127.0.0.1", 80);
      os = new PrintStream(socket.getOutputStream(), true);
      is = new Scanner(socket.getInputStream());
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host.");
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to host");
    }

    try {
      String inputLine;
      String valores[];
      String retorno;
      int x;
      int y;
      int flag;

      do {
        System.out.println(inputLine=is.nextLine());
        valores = inputLine.split("_");
        x = Integer.parseInt(valores[0]);
        y = Integer.parseInt(valores[1]);
        flag = Integer.parseInt(valores[2]);
        
        System.out.println("X: "+x+" Y: "+y+"  Flag: "+flag+"    input: "+inputLine);
      } while (!inputLine.equals (""));

      paraThread = true;
      os.close();
      is.close();
      socket.close();
    } catch (UnknownHostException e) {
      System.err.println("Trying to connect to unknown host: " + e);
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
    }
  }

  public void begin (String ip) {
    this.ip = ip;
  }
  
  public void send(int x, int y, int flag, int id) {
    Scanner tecl = new Scanner(System.in);
    os.println(x+"_"+y+"_"+flag+"_"+id);
  }
}

