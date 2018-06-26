/*
BufferedImage myPicture = ImageIO.read(new File("path-to-file"));
JLabel picLabel = new JLabel(new ImageIcon(myPicture));
add(picLabel);
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.io.*;
import javax.imageio.*;

class Jogo extends JFrame {
    Cliente c;
    Casa [][] casas;

    Jogo (int x, int y, Cliente cliente) {
        super("Trabalho");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        c = cliente;

        JPanel tabela = new JPanel(new GridLayout(x,y));
        casas = new Casa[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Casa btn = new Casa (i, j);
                btn.addMouseListener(new MouseListener(){
                
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        
                    }
                
                    @Override
                    public void mousePressed(MouseEvent e) {
                        switch (e.getButton()) {
                            case 1: // Left button
                                System.out.print("Left (<-) ");
                                cliente.send(btn.x, btn.y, -2);
                                break;

                            case 3: // Right button
                                System.out.print("Right (->) ");
                                cliente.send(btn.x, btn.y, -1);
                                break;

                            default:
                                System.out.println(e.getButton());
                                break;
                        }
                        //System.out.println(e);
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

                tabela.add(btn);
                casas[i][j] = btn;
            }
        }
        add(tabela);
        setSize(500, 500);
        //pack(); // Chama o public Dimension getPreferredSize() para saber o tamanho preferido
        setVisible(true);
    }

    /*public Dimension getPreferredSize() {
        return new Dimension(1000, 600); // Tamanho preferido (Tamanho que vai abrir por padrão)
    }*/

    public void receberPosicao (int x, int y, int flag, int id) {
        if (id == c.getMyId()) { // Foi vc q marcou
            casas[x][y].setForeground(Color.BLACK);

        } else if (id == -1) { // Foi o servidor q marcou
            casas[x][y].setForeground(Color.GRAY);

        } else { // Foi o oponente q marcou
            casas[x][y].setForeground(Color.RED);
        }

        switch (flag) {
            case -1: // Bomba
                casas[x][y].setText("\uD83D\uDCA3");
                break;

            case -2: // Marcação
                casas[x][y].setText("\u2691");
                break;

            default: // Todo o resto
                casas[x][y].setText(flag+"");
                break;
        }
    }
}
