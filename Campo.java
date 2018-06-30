import java.util.*;

public class Campo {

  /*
   
   Valores:
    -2 - marcado
    -1 - bomba
     0 - casa vazia
     1-8 - quantidade de bombas ao redor da casa
  */

  int valor; // O valor da casa, como consta na flag
  boolean exibido; // Se já foi exibido alguma vez
  int marcadoPor; // Id de quem marcou a casa

  Campo (int valor, boolean exibido) {
    this.valor = valor;
    this.exibido = exibido;
    marcadoPor = -1;
  }

  int getValor () {
    return valor;
  }

  void setValor (int valor) {
    this.valor = valor;
  }

  boolean getExibido () {
    return exibido;
  }

  void setExibido (boolean exibido) {
    this.exibido = exibido;
  }

  void setMarcadoPor (int id) {
    marcadoPor = id;
  }

  int getMarcadoPor () {
    return marcadoPor;
  }
}
