import java.io.*;
import java.net.*;
import java.security.*;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Conexión establecida con el servidor.");

            // Recibir clave pública del servidor
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            PublicKey serverPublicKey = (PublicKey) in.readObject();

            // Generar par de claves
            KeyPair keyPair = MyCryptoUtils.randomGenerate(2048);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Enviar clave pública al servidor
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(publicKey);
            out.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while ((message = reader.readLine()) != null) {
                // Encriptar mensaje con clave pública del servidor
                byte[] encryptedMessage = MyCryptoUtils.encryptData(message.getBytes(), serverPublicKey);

                // Enviar mensaje encriptado al servidor
                out.writeObject(encryptedMessage);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
