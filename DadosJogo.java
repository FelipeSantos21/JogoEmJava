import java.net.*;
import java.io.*;
import java.util.*;

public class DadosJogo {
  public Socket socket;
  private int id;
  public PrintStream os;
  public Scanner is;
  private int bombasAchadas;
  
  public void DadosJogo (int id, Scanner is, PrintStream os) {
    this.id = id;
    this.is = is;
    this.os = os;
    bombasAchadas = 0;
  }
  
  public void DadosJogo (int id, PrintStream os) {
    this.id = id;
    this.is = null;
    this.os = os;
    bombasAchadas = 0;
  }

  public void DadosJogo () {
    bombasAchadas = 0;
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
}
