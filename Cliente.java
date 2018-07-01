import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente extends Thread {
  static PrintStream os = null;
  static boolean paraThread = false;
  String ip = "107.0.0.1";
  int id = 0;
  DadosJogo me;
  int tamanho[] = {10, 10}; // x, 
  String valores[];
  Jogo jogo;
  Manager manager;

  Cliente (Manager manager) {
    this.manager = manager;
  }

  public void run () {
    
    Socket socket = null;
    Scanner is = null;
    me = new DadosJogo();

    try { // Inicia Conexao
      me.socket = new Socket(ip, 80);
      me.os = new PrintStream(me.socket.getOutputStream(), true);
      me.is = new Scanner(me.socket.getInputStream());
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host.");
      manager.exit();
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to host");
      manager.exit();
    }
    
    
    try {
      String inputLine;
      String retorno;

      // Recebe os dados iniciais
      inputLine=me.is.nextLine();
      //System.out.println(inputLine);
      this.id = Integer.parseInt(inputLine);
      //System.out.println("This.id == " + this.id);

      jogo = new Jogo (10,10, this, manager); // Inicia o jogo e passa a instancia desse cliente para ele.

      do { // Espera pelo inicio do jogo.
        inputLine=me.is.nextLine();
        //System.out.println(inputLine);
      } while (!inputLine.equals("I"));
      System.out.println("Client -> Inicia Jogo");
      jogo.inicia(true);
      
      // Inicio do Jogo, passa a esperar receber os valores e coordenadas das casas
      do {
        inputLine=me.is.nextLine();
        //System.out.println("Client::  "+ inputLine); // Recebe os dados emitidos pelo servidor
        valores = inputLine.split(":");

        switch (valores[0]) {
          case "T": // Atualizar o timer
            t(valores[1]);
            break;
            
          case "P": // Mostrar posição
            p(valores[1]);
            break;
            
          case "D": // Jogo finalizado com empate
            d(valores[1]);
            break;
            
          case "F": // Jogo finalizado
            f(valores[1]);
            break;  
            
          default:
            //System.out.println("Comando nao reconhecido: "+inputLine);
            break;
        }
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

  // Função para atualizar o timer do jogo
  void t (String msg) {
    /*  
      0 - Tempo
      1 - % Tempo
    */
    valores = msg.split("_"); // Divide a String em um array pelo "_"
    jogo.setTime(Integer.parseInt(valores[0]), Integer.parseInt(valores[1]));
  }

  // Função para mandar uma marcação da casa para o jogo
  void p (String msg) {
    /*
      0 - x
      1 - y
      2 - flag
      3 - id de quem pediu a casa
    */
    valores = msg.split("_"); // Divide a String em um array pelo "_"
    jogo.receberPosicao(Integer.parseInt(valores[0]), Integer.parseInt(valores[1]), Integer.parseInt(valores[2]), Integer.parseInt(valores[3]));
  }

  // Função para enviar um fim de jogo com empate para o jogo
  void d (String msg) {
    /*
      0 - Bombas Marcadas
      1 - Bombas Erradas
    */
    valores = msg.split("_"); // Divide a String em um array pelo "_"
    jogo.empate(Integer.parseInt(valores[0]), Integer.parseInt(valores[1]));
  }

  // Função para mandar o fim de jogo para o jogo
  void f (String msg) {
    /* 
      0 - idVencedor
      1 - Bombas Achadas Vencedor
      2 - Bombas Erradas Vencedor
      3 - Bombas Achadas Perdedor
      4 - Bombas Erradas Perdedor
    */
    valores = msg.split("_");
    jogo.fimJogo ((Integer.parseInt(valores[0]) == id), Integer.parseInt(valores[1]), Integer.parseInt(valores[2]), Integer.parseInt(valores[3]), Integer.parseInt(valores[4]));
  }

  public void send(int x, int y, int flag) {
    me.os.println("P:"+x+"_"+y+"_"+flag+"_"+id);
  }

  public int getMyId() {
    return id;
  }

  public void setIp (String ip) {
    this.ip = ip;
  }
}

