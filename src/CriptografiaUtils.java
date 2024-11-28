import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;

public class CriptografiaUtils {

    private static final byte[] SECRET_KEY = "chave12345678901".getBytes();
    private static final byte[] IV = new byte[16];

    static {
        new SecureRandom().nextBytes(IV);
    }

    public static void criptografarArquivo(File inputFile) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY, "AES");
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(tempFile)) {

            fos.write(iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }

            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                fos.write(outputBytes);
            }
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao substituir o arquivo original pelo criptografado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            throw new Exception("Erro ao substituir o arquivo original pelo criptografado.");
        }

        System.out.println("Arquivo criptografado com sucesso: " + inputFile.getAbsolutePath());
        
        JOptionPane.showMessageDialog(
                null,
                "Arquivo criptografado com sucesso: " + inputFile.getAbsolutePath(),
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    
    
    public static void descriptografarArquivo(File inputFile) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY, "AES");

        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(tempFile)) {

            byte[] iv = new byte[16];
            if (fis.read(iv) != iv.length) {
                JOptionPane.showMessageDialog(
                        null,
                        "Arquivo corrompido: não foi possível ler o arquivo.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                throw new IllegalArgumentException("Arquivo corrompido: não foi possível ler o IV.");
            }
            IvParameterSpec ivParams = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }

            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                fos.write(outputBytes);
            }
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao substituir o arquivo criptografado pelo original.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            throw new Exception("Erro ao substituir o arquivo criptografado pelo original.");
        }

        System.out.println("Arquivo descriptografado com sucesso: " + inputFile.getAbsolutePath());
        
        JOptionPane.showMessageDialog(
                null,
                "Arquivo descriptografado com sucesso: " + inputFile.getAbsolutePath(),
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    
    
    
    
    
}
