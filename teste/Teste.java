import java.net.InetAddress;
import java.net.UnknownHostException;
 
public class Teste {
 
    public static void main(String a[]){
     
        try {
            InetAddress ipAddr = InetAddress;
            System.out.println(ipAddr);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }
}