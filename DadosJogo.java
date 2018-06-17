import java.net.*;
import java.io.*;
import java.util.*;

public class DadosJogo {
  public Socket socket;
  private int id;
  public PrintStream os;
  public Scanner is;
  private int bombasAchadas;
  private int bombasErradas;
  
  void DadosJogo () {
    bombasAchadas = 0;
    bombasErradas = 0;
  }
  
  void DadosJogo (int id, Scanner is, PrintStream os) {
    this.id = id;
    this.is = is;
    this.os = os;
    bombasAchadas = 0;
    bombasErradas = 0;
  }

  int getId () {
    return id;
  }

  void setId (int id) {
    this.id = id;
  }

  void incrementBombasAchadas () {
    bombasAchadas++;
  }

  int getBombasAchadas () {
    return bombasAchadas;
  }

  void incrementBombasErradas () {
    bombasErradas++;
  }

  int getBombasErradas () {
    return bombasErradas;
  }
}
