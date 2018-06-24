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
  
  int naoUsado = -5;
  Socket clientSocket;
  public static DadosJogo clientes[] = new DadosJogo[3];
  public static int cont=-1;
  String valores[];
  int x;
  int y;
  int flag;
  int id;  
  Campo campo = new Campo();
  boolean primeiroClick = true;
  boolean enviarDisponivel = false;

  Servindo(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public void run() {

    try {
      Scanner is = new Scanner(clientSocket.getInputStream());
      cont++;
      clientes[cont] = new DadosJogo(); //(cont, new PrintStream(clientSocket.getOutputStream()));
      System.out.println("cont: "+cont);
      clientes[cont].setId(cont);
      clientes[cont].os = new PrintStream(clientSocket.getOutputStream());

      String inputLine, outputLine;
      
      clientes[cont].os.println(cont); // Envia o id para o novo cliente
      clientes[cont].os.flush();

      do {
        enviarDisponivel = true;
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

        pesquisarPorBombas(x, y, id);
      } while (!inputLine.equals(""));

      enviarDisponivel = false;
      for (int i=0; i<cont; i++)
        clientes[i].os.close();
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
    if (!enviarDisponivel)
      return;

    for (int i=0; i<cont; i++) {
      clientes[i].os.println(msg);
      clientes[i].os.flush();
    }
    System.out.print(msg+'\n');
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
    {naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado},
    {naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado},
    {naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado},
    {naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado},
    {naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado},
    {naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado},
    {naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado},
    {naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado},
    {naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado},
    {naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado,naoUsado}
  };

  void criarCampo (int x, int y) {
    int xb, yb;
    System.out.println("\n\n\n########### Criar Campo ("+x+", "+y+") ###############");
    for (int i = 0; i < 13; i++) {
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
    imprimirCampo();
  }
  
  int pesquisarPorBombas (int x, int y, int id) {
    if (x < 0 || x > 9 || y < 0 || y > 9) {
      return -200;
    }

    /* ordem de busca: casa, topo, topo/direita, direita, baixo/direita, baixo, baixo/esquerda, esquerda, topo/esquerda */
    int numeroCasa = 0;
    System.out.println("\n\n\n########### Pesquisar por bombas ("+x+", "+y+") ###############");
    if (mCampo[x][y] != naoUsado) {
      return mCampo[x][y];
    }

    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        if ( !((x+i < 0) || (x+i >= 10) || (y+j) < 0 || (y+j >= 10)) ){ // Testa se a posição que vai ser checada na matriz é valida.
          if (mCampo[x+i][y+j] == -1) {
            numeroCasa++;
          }
        }
      }
    }
    mCampo[x][y] = numeroCasa;
    
    System.out.print("mCampo["+x+"]["+y+"] = "+numeroCasa+"\n");
    if (numeroCasa == 0) {
      for (int i = -1; i < 2; i++) {
        
        for (int j = -1; j < 2; j++) {
          if ( !((x+i < 0) || (x+i >= 10) || (y+j) < 0 || (y+j >= 10)) ){ // Testa se a posição que vai ser checada na matriz é valida.
            
            if (mCampo[x+i][y+j] == naoUsado) {
              pesquisarPorBombas(x+i, y+j, -1);
            }
          }
        }
      }
    }

    enviar(x+"_"+y+"_"+numeroCasa+"_"+id);
    
    return numeroCasa;
  }

  void imprimirCampo () {
    
    System.out.print("\n\n");
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        System.out.print("|"+mCampo[i][j]+"| ");
      }
      System.out.print("\n");
    }
    System.out.print("\n\n");
  }
};