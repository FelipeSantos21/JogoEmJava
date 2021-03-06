import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.SwingUtilities;

class Jogo extends JFrame {
	Cliente c;
    Casa [][] casas;
    JProgressBar tempoP;
    JLabel tempoN;
    int x;
    int y;

    Jogo (int x, int y, Cliente cliente, Manager manager) {
        super("Trabalho");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.x = x;
        this.y = y;
        c = cliente;
        tempoP = new JProgressBar();
        tempoN = new JLabel();

        JPanel painelTempo = new JPanel (new BorderLayout());
        tempoN.setText("Tempo: 0 segundos ");
        tempoP.setValue(0);
        tempoP.setStringPainted(true);

        painelTempo.add(tempoN, BorderLayout.WEST);
        painelTempo.add(tempoP, BorderLayout.CENTER);

        add(painelTempo, BorderLayout.NORTH);

        JPanel tabela = new JPanel(new GridLayout(x,y));
        casas = new Casa[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Casa btn = new Casa (i, j, false);
                btn.addMouseListener(new MouseListener(){

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (btn.getMyEnable()) {
                            switch (e.getButton()) {
                                case 1: // Left button
                                    //System.out.print("Left (<-) ");
                                    cliente.send(btn.x, btn.y, -1);
                                    break;

                                case 3: // Right button
                                    //System.out.print("Right (->) ");
                                    cliente.send(btn.x, btn.y, -2);
                                    break;

                                default:
                                    //System.out.println(e.getButton());
                                    break;
                            }
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }
                });
                btn.setMyEnable(false);
                btn.setEnabled(false);
                tabela.add(btn);
                casas[i][j] = btn;
            }
        }
        add(tabela, BorderLayout.CENTER);
        setSize(500, 500);
        setVisible(true);
    }

    public void receberPosicao (int x, int y, int flag, int id) {
        casas[x][y].setMyEnable(false); // Impede que o jogador mande algum comando via clique para o botão

        if (id == c.getMyId()) { // Foi vc q marcou
            casas[x][y].setForeground(Color.BLACK);

        } else if (id == -1) { // Foi o servidor q marcou
            casas[x][y].setForeground(Color.GRAY);

        } else { // Foi o oponente q marcou
            casas[x][y].setForeground(Color.RED);
        }

        switch (flag) {
            case -1: // Bomba
                casas[x][y].setText("X"); //  "\uD83D\uDCA3");
                break;

            case -2: // Marcação
                casas[x][y].setText("\u2691");
                break;

            case -5: // Caso a casa esteja como não usada no servidor
                casas[x][y].setText("");
                break;


            default: // Todo o resto
                casas[x][y].setText(flag+"");
                break;
        }
    }

    public void inicia (boolean destrava) {
        //System.out.println("Jogo -> Inicia Jogo: "+destrava);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                casas[i][j].setEnabled(destrava);
                casas[i][j].setMyEnable(destrava);
            }
        }
    }

    public void setTime(int tempo, int porcentagem) {
        // Ambos em milisegundos

        tempoN.setText("Tempo: "+tempo+" segundos ");
        tempoP.setValue(porcentagem);
    }

    public void empate (int bombasAchadas) {
        inicia(false);
        JFrame fimDeJogo = new JFrame("Resultado");
        fimDeJogo.setLayout(new BorderLayout());
        fimDeJogo.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel resultado = new JPanel (new GridLayout(1, 2));
        resultado.add(new JLabel("Bombas Acertadas"));
        resultado.add(new JLabel(bombasAchadas+""));

        fimDeJogo.add(new JLabel("EMPATE!"), BorderLayout.NORTH);
        fimDeJogo.add(resultado, BorderLayout.CENTER);
        fimDeJogo.pack();
        fimDeJogo.setVisible(true);
    }

    public void fimJogo (boolean vcVenceu, int bombasAchadasVencedor, int bombasAchadasPerdedor) {
        inicia(false);
        JFrame fimDeJogo = new JFrame("Resultado");
        fimDeJogo.setLayout(new BorderLayout());
        fimDeJogo.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel resultado = new JPanel (new GridLayout(3, 2));
        resultado.add(new JLabel(""));
        resultado.add(new JLabel("Bombas Acertadas"));

        if (vcVenceu){
            resultado.add(new JLabel("Voce venceu!"));
        } else {
            resultado.add(new JLabel("Oponente venceu!"));
        }
        resultado.add(new JLabel(bombasAchadasVencedor+""));

        if (vcVenceu){
            resultado.add(new JLabel("Oponente perdeu!"));
        } else {
            resultado.add(new JLabel("Voce perdeu!"));
        }
        resultado.add(new JLabel(bombasAchadasPerdedor+""));

        fimDeJogo.add(new JLabel("Resultado do Jogo:"), BorderLayout.NORTH);
        fimDeJogo.add(resultado, BorderLayout.CENTER);
        fimDeJogo.pack();
        fimDeJogo.setVisible(true);
    }

}