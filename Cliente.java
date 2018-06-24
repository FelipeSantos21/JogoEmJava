import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente extends Thread {
  static PrintStream os = null;
  static boolean paraThread = false;
  String ip = "107.0.0.1";
  int id = 0;
  DadosJogo me;
  int tamanho[] = {10, 10}; // x, y

  public void run () {
    
    Socket socket = null;
    Scanner is = null;
    me = new DadosJogo();

    try { // Inicia Conexao
      me.socket = new Socket("127.0.0.1", 80);
      me.os = new PrintStream(me.socket.getOutputStream(), true);
      me.is = new Scanner(me.socket.getInputStream());
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
      int idRecebido;

      // Recebe os dados iniciais
      System.out.println(inputLine=me.is.nextLine());
      this.id = Integer.parseInt(inputLine);
      System.out.println("This.id == " + this.id);

      Jogo jogo = new Jogo (10,10, this); // Inicia o jogo e passa a instancia desse cliente para ele.

      // Inicio do Jogo, passa a esperar receber os valores e coordenadas das casas
      do {
        System.out.println(inputLine=me.is.nextLine()); // Recebe os dados emitidos pelo servidor
        valores = inputLine.split("_"); // Divide a string em um array pelo "_"
        x = Integer.parseInt(valores[0]);
        y = Integer.parseInt(valores[1]);
        flag = Integer.parseInt(valores[2]);
        idRecebido = Integer.parseInt(valores[3]);
        
        System.out.println("X: "+x+" Y: "+y+"  Flag: "+flag+" ID: "+idRecebido+"    input: "+inputLine);
        jogo.receberPosicao(x, y, flag, idRecebido);
      } while (!inputLine.equals (""));

      paraThread = true;
      me.os.close();
      me.is.close();
      me.socket.close();
    } catch (UnknownHostException e) {
      System.err.println("Trying to connect to unknown host: " + e);
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
    }
  }
  
  public void send(int x, int y, int flag) {
    Scanner tecl = new Scanner(System.in);
    me.os.println(x+"_"+y+"_"+flag+"_"+id);
  }
  
  public void Cliente (String ip, int id) {
    this.ip = ip;
    this.id = id;
  }
  public void Cliente () {}

  public int getMyId() {
    return id;
  }
}

