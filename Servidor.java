import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

class Servidor extends Thread{
  public void run () {
    ServerSocket serverSocket=null;

    try {
      serverSocket = new ServerSocket(80);
    } catch (IOException e) {
      System.out.println("Could not listen on port: " + 80 + ", " + e);
      JOptionPane.showMessageDialog(null, "Não foi possivel abrir a sala!\nDeve ter uma sala sala aberta nesse computador\n IP: 107.0.0.1");
      System.exit(1);
    }

    for (int i=0; i<2; i++) {
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
  
  final int naoUsado = -5;
  Socket clientSocket;
  public static DadosJogo clientes[] = new DadosJogo[2];
  public static int cont=-1;
  public static boolean enviarDisponivel = false;
  String valores[];
  int x;
  int y;
  int flag;
  int id;  
  //Campo campo = new Campo();
  boolean primeiroClick = true;

  int tempo = 0;
  final int tempoMax = 600000; // Em milisegundos
  final int tempoPasso = 500; // Em milisegundos
  Timer timer;

  Servindo(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  class tempoJogo extends TimerTask { // Classe para incrementar o timer
    public void run() {
        tempo = tempo + tempoPasso;

        if (tempo > tempoMax) {
          fimJogo(0, false);
          return;
        }

        enviar("T:"+((tempoMax - tempo)/1000)+"_"+(tempo*100/tempoMax));
    }
  }

  public void run() {

    try {
      Scanner is = new Scanner(clientSocket.getInputStream());

      if (cont++ > 1) {
        cont--;
      } else {
        clientes[cont] = new DadosJogo(); //(cont, new PrintStream(clientSocket.getOutputStream()));
        System.out.println("cont: "+cont);
        clientes[cont].setId(cont);
        clientes[cont].os = new PrintStream(clientSocket.getOutputStream());
      
        clientes[cont].os.println(cont); // Envia o id para o novo cliente
        clientes[cont].os.flush();
        if (cont == 1) {
          enviar("I"); // Envia o comando para iniciar o jogo.
          System.out.println("Server -> Iniciar Jogo");
          timer = new Timer(true);
          timer.schedule(new tempoJogo(), tempoPasso, tempoPasso); // Para executar mais de uma vez
        }
      }

      String inputLine, outputLine;

      do {
        enviarDisponivel = true;
        inputLine = is.nextLine();
        switch (inputLine.split(":")[0]) {
          case "P":
            p(inputLine.split(":")[1]);
            break;
            
          default:
            System.out.println("Server recebeu comando nao estendido: "+inputLine);
            break;
        }
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

  void p (String msg) { // Função chamada para passar a posição da jogada, que ao enviar é representada pelo caractere 'P'
    valores = msg.split("_");
    x = Integer.parseInt(valores[0]);
    y = Integer.parseInt(valores[1]);
    flag = Integer.parseInt(valores[2]);
    id = Integer.parseInt(valores[3]);
    
    if (primeiroClick) {
      criarCampo(x, y);
      primeiroClick = false;
    }
    if (flag == -1) { // Caso seja para abrir a casa
      pesquisarPorBombas(x, y, id);

    } else if (flag == -2 && !primeiroClick) {
      marcarFlag(x, y, id);
    }
  }

  void enviar(String msg) {
    // Envia a String para os clientes
    if (!enviarDisponivel)
      return;

    for (int i=0; i<=cont; i++) {
      clientes[i].os.println(msg);
      clientes[i].os.flush();
    }
    System.out.print(msg+'\n');
  }

  
  /*
   * Flag - Recebido do Cliente:
   -1 - descobrir
   -2 - marcar
   
   * Flag - Retorno para o Cliente:
   -2 - marcado
   -1 - bomba
   0 - casa vazia
   1-8 - quantidade de bombas ao redor da casa
  */

  Campo[][] mCampo = null;

  

  void criarCampo (int x, int y) {
    if (mCampo != null) {
      System.out.println("Campo já criado!");
      return;
    }

    mCampo = new Campo[10][10];

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        mCampo[i][j] = new Campo(naoUsado, false); 
        enviar("P:"+x+"_"+y+"_-5_-1");
      }
    }

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
        mCampo[xb][yb].setValor(-1);
      }
    }
    imprimirCampo(false);
  }
  
  int pesquisarPorBombas (int x, int y, int id) {
    if (x < 0 || x > 9 || y < 0 || y > 9) { // Testo se a posição passada é válida
      return -200;
    }
    /* ordem de busca: casa, topo, topo/direita, direita, baixo/direita, baixo, baixo/esquerda, esquerda, topo/esquerda */

    int numeroCasa = 0;
    System.out.println("\n\n\n########### Pesquisar por bombas ("+x+", "+y+") ###############");
    if (mCampo[x][y].getExibido()) { // Caso essa casa já tenha sido exibida para o usuário e por tando calculada
      return mCampo[x][y].getValor();
    }

    if (mCampo[x][y].getValor() == -1) { // Caso tenha abrido uma bomba
      if (id == -1) { // Caso o servidor tenha chamado
        enviar("P:"+x+"_"+y+"_-1_-1");
        return -1;

      } else { // Caso um dos jogadores tenha chamado
        enviar("P:"+x+"_"+y+"_"+-1+"_"+id);
        fimJogo(id, true);
        return -1;
      }
    }

      // Ia colocar alguma coisa aqui só não lembro o que e se o bloco de cima já resolve o problema...

    for (int i = -1; i < 2; i++) { // Calcula o numero da casa (quantidade de bombas perimetro de uma casa a partir dela)
      for (int j = -1; j < 2; j++) {
        if ( !((x+i < 0) || (x+i >= 10) || (y+j) < 0 || (y+j >= 10) || (i==0 && j==0))){ // Testa se a posição que vai ser checada na matriz é valida.
          if (mCampo[x+i][y+j].getValor() == -1) { // Testa se é uma bomba para incrementar no contador numeroCasa
            numeroCasa++;
          }
        }
      }
    }
    mCampo[x][y].setValor(numeroCasa);
    mCampo[x][y].setExibido(true);
    enviar("P:"+x+"_"+y+"_"+numeroCasa+"_"+id);

    System.out.print("mCampo["+x+"]["+y+"] = "+numeroCasa+"\n");
    if (numeroCasa == 0) {
      for (int i = -1; i < 2; i++) {
        
        for (int j = -1; j < 2; j++) {
          if ( !((x+i < 0) || (x+i >= 10) || (y+j) < 0 || (y+j >= 10) || (i==0 && j==0)) ){ // Testa se a posição que vai ser checada na matriz é valida.
            
            if (mCampo[x+i][y+j].getValor() == naoUsado){//} && mCampo[x+i][y+j].getMarcadoPor() == -1) {
              pesquisarPorBombas(x+i, y+j, -1);
            }
          }
        }
      }
    }
    return numeroCasa;
  }

  void marcarFlag (int x, int y, int id) {
    if(mCampo[x][y].getExibido()) {
      return;
    }

    System.out.println("Marcar Flag -> ID: "+id);
    if (-1 < id && id <= cont) {
      if (mCampo[x][y].getMarcadoPor() != -1) {
        return;
      }

      enviar("P:"+x+"_"+y+"_-2_"+id);
      mCampo[x][y].setMarcadoPor(id);

      
      if (mCampo[x][y].getValor() == -1) {
        clientes[id].incrementBombasAchadas();
      } else {
        clientes[id].incrementBombasErradas();
      }
    }
    checarSeMCampoEstaCheio();
  }

  void imprimirCampo (boolean enviar) {
    
    System.out.print("\n\n");
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        System.out.print("|"+mCampo[i][j].getValor()+"| ");
        if (enviar) {
          enviar("P:"+i+"_"+j+"_"+mCampo[i][j].getValor()+"_"+id);
        }
      }
      System.out.print("\n");
    }
    System.out.print("\n\n");
  }

  void checarSeMCampoEstaCheio () {
    int usados = 0;

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (mCampo[i][j].getExibido() || mCampo[i][j].getMarcadoPor() != -1) {
          usados++;
        }
      }
    }

    if (usados >= 100) {
      fimJogo(-1, false);
    }
  }

  void fimJogo(int id, boolean bomba) {
    timer.cancel(); //Finalizar a thread do timer
    imprimirCampo(true);
    if (!bomba) { // Caso tenha acabado por tempo ou por ter acabado o campo
      if (clientes[0].getBombasAchadas() == clientes[1].getBombasAchadas()) { // Caso a primeira concição dê empate
        if  (clientes[0].getBombasErradas() == clientes[1].getBombasErradas()) { // Caso a segunda concição dê empate
          enviar ("D:"+clientes[0].getBombasAchadas()+"_" + clientes[0].getBombasErradas());
          return;

        } else { // Caso a segunda condição consiga desempatar
          if (clientes[0].getBombasErradas() > clientes[1].getBombasErradas()) {  // Caso o primeiro jogador seja o vencedor
            id = 1;
          } else {  // Caso o segundo jogador seja o vencedor
            id = 0;
          }
        }

      } else { // Caso a primeira condição seja o suficiente para decidir o ganhador
        if (clientes[0].getBombasAchadas() > clientes[1].getBombasAchadas()) {
          id = 1;
        } else {
          id = 0;
        }
      }
    }  

    if (id == 1) { // o id representa o perdedor
      enviar ("F:"+0+"_" + clientes[0].getBombasAchadas()+"_" + clientes[0].getBombasErradas()+"_"+ clientes[1].getBombasAchadas()+"_" + clientes[1].getBombasErradas());
    } else {
      enviar ("F:"+1+"_" + clientes[1].getBombasAchadas()+"_" + clientes[1].getBombasErradas()+"_"+ clientes[0].getBombasAchadas()+"_" + clientes[0].getBombasErradas());
    }
  }
};