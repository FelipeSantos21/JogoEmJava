import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

import java.io.*;
import javax.imageio.*;
import java.lang.*;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class Manager {
    JFrame telaInicial;
    String strIp = "";

    public static void main (String[] args) {
        System.out.println("Iniciado!");
        Manager m = new Manager();
        m.comeca();
    }

    public void comeca () {
        telaInicial = new JFrame();
        //telaInicial.setDefaultCloseOperation(EXIT_ON_CLOSE);
        telaInicial.setTitle("Tela Inicial");
        telaInicial.add(new JLabel("Escolha entre inicar uma sala e entrar em uma sala"), BorderLayout.NORTH);
        
        JButton btnNovaSala = new JButton("Nova Sala");
        btnNovaSala.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                iniciaJogo(true);
            }
        });
        telaInicial.add(btnNovaSala, BorderLayout.WEST);
        
        JButton btnSalaAberta = new JButton("Entrar em uma sala criada");
        btnSalaAberta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                getIp();
            }
        });
        telaInicial.add(btnSalaAberta, BorderLayout.EAST);
        
        telaInicial.pack( );
        telaInicial.setVisible(true);
        System.out.println("Tela iniciada!");
    }

    
    private void mostrarIp () {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "O ip da sala e: " + address.getHostAddress());
    }

    public void iniciaJogo (boolean novaSala) {
        Servidor serv = null;
        Cliente cli = null;
        System.out.println("IniciaJogo -> novaSala: "+novaSala);
        if (novaSala) {
            serv = new Servidor();
            serv.start();
            mostrarIp();
            cli = new Cliente(this);

        }  else { // Entra em uma sala existente.            
            System.out.println("IP: "+strIp);
            cli = new Cliente(this);
            cli.setIp (strIp);
        } 
        
        cli.start(); // Inicia com o ip do localhost
        telaInicial.setVisible(false);
    }

    void getIp () {
        JTextField ip[] = new JTextField[4];
        JFrame telaIp = new JFrame();
        telaIp.setTitle("Ip");
        telaIp.add(new JLabel("Insira o ip da sala!"), BorderLayout.NORTH);
        
        JPanel campoIp = new JPanel(new GridLayout(1, 4));

        JPanel campo = new JPanel(new BorderLayout());
        ip[0] = new JTextField("127", 3);
        campo.add(ip[0], BorderLayout.CENTER);
        campo.add(new JLabel("."), BorderLayout.EAST);
        campoIp.add(campo);

        campo = new JPanel(new BorderLayout());
        ip[1] = new JTextField("0", 3);
        campo.add(ip[1], BorderLayout.CENTER);
        campo.add(new JLabel("."), BorderLayout.EAST);
        campoIp.add(campo);

        campo = new JPanel(new BorderLayout());
        ip[2] = new JTextField("0", 3);
        campo.add(ip[2], BorderLayout.CENTER);
        campo.add(new JLabel("."), BorderLayout.EAST);
        campoIp.add(campo);

        ip[3] = new JTextField("1", 3);
        campoIp.add(ip[3]);
        
        telaIp.add(campoIp, BorderLayout.CENTER);
        
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                boolean valido = true;
                for (int i = 0; i < 4; i++) {
                    
                    int numIp;  
                    try {
                        numIp = Integer.parseInt(ip[i].getText());
                        if (0 <= numIp && numIp > 255) {
                            ip[i].setText("");
                            valido = false;
                        }
                    }
                    catch (NumberFormatException ex){
                        ip[i].setText("");
                        valido = false;
                    }
                }
                
                if (valido) {
                    strIp = ip[0].getText()+"."+ip[1].getText()+"."+ip[2].getText()+"."+ip[3].getText();
                    telaIp.setVisible(false);
                    iniciaJogo(false);
                } else {
                    JOptionPane.showMessageDialog(null, "O ip não é válido!");
                }
            }
        });

        telaIp.add(btnEntrar, BorderLayout.EAST);
        telaIp.pack();
        telaIp.setVisible(true);
    }

    public void exit () {
        System.exit(1);
    }
}