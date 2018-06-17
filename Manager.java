import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.io.*;
import javax.imageio.*;
import java.lang.*;

public class Manager {
    JFrame telaInicial;

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
                //iniciaJogo(false);
            }
        });
        telaInicial.add(btnSalaAberta, BorderLayout.EAST);
        
        telaInicial.pack( );
        telaInicial.setVisible(true);
        System.out.println("Tela iniciada!");
    }

    public void iniciaJogo (boolean novaSala) {
        Servidor serv = null;
        Cliente cli = null;

        if (novaSala) {
             serv = new Servidor();
             serv.start();
             cli = new Cliente();

        } /* else {
        	String newIp = JOptionPane.showInputDialog("Entre com o ip da sala EX: 192.168.0.2");;
        	
        	 while (!ipIsValid(newIp)) {
        		newIp = JOptionPane.showInputDialog("Ip invalido!\nEntre com o ip da sala EX: 168.192.0.2");
        	} 
            
            System.out.println("IP: "+newIp);
            cli = new Cliente(ip, id=1); //newIp);
        } */
        
        cli.start(); // Inicia com o ip do localhost
        telaInicial.setVisible(false);
    }
    
    /*
    public boolean ipIsValid (String ip) {
       String[] ipParts = ip.split(".");

       System.out.println("\n\n");
    	if (partsIp.length != 4) { return false; } // caso n�o tenha os 3 pontos para separar os numeros retorna como errado
    	
        for (int i = 0; i < 4; i++) { // Caso uma das casas do ip seja maior que o intervalo [0 , 255] retorna como errado
            System.out.println(partsIp[i]);
    		if (partsIp[i] < 0 || partsIp[i] > 255) {
    			return false;
    		}
    	}
    	
    	return true;    
    } */

    
}