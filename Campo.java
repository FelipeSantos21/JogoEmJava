import java.util.*;

public class Campo {

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
      
      if (( y-1 <= yb|| yb <= y+1) && ( x-1 <= xb|| xb <= x+1)) {
        i--;
        System.out.print("\t");
      } else {
        mCampo[xb][yb] = -1;
      }
    }
  }
  
  int pesquisarPorBombas (int x, int y, int id) {
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
          pesquisarPorBombas(i, j, -1);
        }
      }
    }
    return numeroCasa;
  }
}
