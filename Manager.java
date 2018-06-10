import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.io.*;
import javax.imageio.*;

public class Manager {
    
    public static void main (String[] args) {
        System.out.println("Iniciado!");
        Thread t = new Thread(new Cliente());
        t.start();
        Manager m = new Manager();
        m.comeca();
    }

    public void comeca () {
        JFrame telaInicial = new JFrame();
        //telaInicial.setDefaultCloseOperation(EXIT_ON_CLOSE);
        telaInicial.setTitle("Tela Inicial");
        telaInicial.add(new JLabel("Escolha entre inicar uma sala e entrar em uma sala"), BorderLayout.NORTH);
        
        JButton btnNovaSala = new JButton("Nova Sala");
        btnNovaSala.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("Teste Btn");
            }
        });
        telaInicial.add(btnNovaSala, BorderLayout.WEST);
        
        JButton btnSalaAberta = new JButton("Entrar em uma sala criada");
        btnSalaAberta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("Teste Btn");
            }
        });
        telaInicial.add(btnSalaAberta, BorderLayout.EAST);
        
        telaInicial.pack( );
        telaInicial.setVisible(true);
        System.out.println("Tela iniciada!");
    }
}
