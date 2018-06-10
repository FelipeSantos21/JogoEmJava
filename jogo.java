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

class Casa extends JButton{
    public int x;
    public int y;

    Casa  (int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Jogo extends JFrame {
    

    Jogo (int x, int y) {
        super("Trabalho");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel tabela = new JPanel(new GridLayout(x,y));
        Casa [][] casas = new Casa[x][y];
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
                                break;

                            case 3: // Right button
                                System.out.print("Right (->) ");
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
        pack(); // Chama o public Dimension getPreferredSize() para saber o tamanho preferido
        setVisible(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1000, 600); // Tamanho preferido (Tamanho que vai abrir por padrão)
      }

    static public void main(String[] args) {
        Jogo j = new Jogo(20,20);
    }
}
