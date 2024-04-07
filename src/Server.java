import java.io.*;
import java.net.*;
import java.security.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor en línea. Esperando conexión...");

            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            // Generar par de claves
            KeyPair keyPair = MyCryptoUtils.randomGenerate(2048);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Enviar clave pública al cliente
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(publicKey);
            out.flush();

            // Recibir clave pública del cliente
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            PublicKey clientPublicKey = (PublicKey) in.readObject();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while ((message = reader.readLine()) != null) {
                // Encriptar mensaje con clave pública del cliente
                byte[] encryptedMessage = MyCryptoUtils.encryptData(message.getBytes(), clientPublicKey);

                // Enviar mensaje encriptado al cliente
                out.writeObject(encryptedMessage);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
