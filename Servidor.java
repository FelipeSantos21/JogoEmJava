import java.net.*;
import java.io.*;
import java.util.*;

class Servidor extends Thread{
  public void run () {
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
  int id;  
  Campo campo = new Campo();
  boolean primeiroClick = true;

  Servindo(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public void run() {
    DadosJogo clientes[] = new DadosJogo[2];

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
        id = Integer.parseInt(valores[3]);
        
        if (primeiroClick) {
          criarCampo(x, y);
          primeiroClick = false;
        }

        retorno = x+"_"+y+"_"+pesquisarPorBombas(x, y)+"_"+id;
        System.out.println("Retorno Servidor ---> "+retorno);
        enviar(retorno);
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

  void enviar(String msg) {
    // Envia a string para os clientes
    for (int i=0; i<cont; i++) {
      os[i].println(msg);
      os[i].flush();
    }
  }

  
  /*
   * Flag - Recebido do Cliente:
   -1 - marcar
   -2 - descobrir
   
   * Flag - Retorno para o Cliente:
   -2 - marcado
   -1 - bomba
   0 - casa vazia
   1-8 - quantidade de bombas ao redor da casa
  */

  int[][] mCampo = { 
    {0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0},
    {0,0,0,0,0,0,0,0,0,0}    
  };

  void criarCampo (int x, int y) {
    int xb, yb;
    System.out.println("\n\n\n########### Criar Campo ("+x+", "+y+") ###############");
    for (int i = 0; i < 10; i++) {
      Random r = new Random();
      xb = r.nextInt(10);
      yb = r.nextInt(10);
      System.out.println("["+i+"]  Xb: "+xb+" Yb: "+yb);
      
      if (( y-1 <= yb && yb <= y+1) && ( x-1 <= xb && xb <= x+1)) {
        i--;
        System.out.print("\t");
      } else {
        mCampo[xb][yb] = -1;
      }
    }
  }
  
  int pesquisarPorBombas (int x, int y) {
    /* ordem de busca: casa, topo, topo/direita, direita, baixo/direita, baixo, baixo/esquerda, esquerda, topo/esquerda */
    int numeroCasa = 0;
    System.out.println("\n\n\n########### Pesquisar por bombas ("+x+", "+y+") ###############");

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (mCampo[i][j] == -1) {
          numeroCasa++;
        }
      }
    }
    mCampo[x][y] = numeroCasa;
    
    System.out.println("mCampo["+x+"]["+y+"] = "+numeroCasa);
    if (numeroCasa == 0) {
      for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          enviar(x+"_"+y+"_"+pesquisarPorBombas(i, j)+"_"+"-1");
        }
      }
    }
    return numeroCasa;
  }
};