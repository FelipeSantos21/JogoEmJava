import java.net.*;
import java.io.*;
import java.util.*;

class Servidor {
  public static void main(String[] args) {
    ServerSocket serverSocket=null;

    try {
      serverSocket = new ServerSocket(80);
    } catch (IOException e) {
      System.out.println("Could not listen on port: " + 80 + ", " + e);
      System.exit(1);
    }

    for (int i=0; i<3; i++) {
      Socket clientSocket = null;
      try {
        clientSocket = serverSocket.accept();
      } catch (IOException e) {
        System.out.println("Accept failed: " + 80 + ", " + e);
        System.exit(1);
      }

      System.out.println("Accept Funcionou!");

      new Servindo(clientSocket).start();

    }

    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}


class Servindo extends Thread {
  Socket clientSocket;
  static PrintStream os[] = new PrintStream[3];
  static int cont=0;
  String valores[];
  int x;
  int y;
  int flag;

  Servindo(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public void run() {
    try {
      Scanner is = new Scanner(clientSocket.getInputStream());
      os[cont++] = new PrintStream(clientSocket.getOutputStream());
      String inputLine, outputLine, retorno;

      do {
        inputLine = is.nextLine();
        
        valores = inputLine.split("_");
        x = Integer.parseInt(valores[0]);
        y = Integer.parseInt(valores[1]);
        flag = Integer.parseInt(valores[2]);

        x += 1; y += 2; flag += 3;
        
        retorno = x+"_"+y+"_"+flag;
        System.out.println("X: "+x+" Y: "+y+"  Flag: "+flag+"    input: "+retorno);

        
        for (int i=0; i<cont; i++) {
          os[i].println(retorno);
          os[i].flush();
        }
      } while (!inputLine.equals(""));

      for (int i=0; i<cont; i++)
        os[i].close();
      is.close();
      clientSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchElementException e) {
      System.out.println("Conexacao terminada pelo cliente");
    }
  }
};
