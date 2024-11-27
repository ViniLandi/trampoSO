import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;

public class CriptografiaUtils {

    // Chave fixa no backend (16 bytes = 128 bits)
    private static final byte[] SECRET_KEY = "chave12345678901".getBytes(); // Substitua pela sua chave fixa
    private static final byte[] IV = new byte[16]; // Vetor de inicialização (pode ser fixo ou gerado aleatoriamente)

    static {
        // Inicializa o vetor IV com valores aleatórios (ou fixos)
        new SecureRandom().nextBytes(IV);
    }

    public static void criptografarArquivo(File inputFile) throws Exception {
        // Configurar o cipher AES no modo CBC com padding PKCS5
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY, "AES");
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv); // Gera IV aleatório
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        // Criação do arquivo temporário para a criptografia
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(tempFile)) {

            // Salvar o IV no início do arquivo criptografado
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

        // Substitui o arquivo original pelo arquivo criptografado
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            throw new Exception("Erro ao substituir o arquivo original pelo criptografado.");
        }

        System.out.println("Arquivo criptografado com sucesso: " + inputFile.getAbsolutePath());
    }

    
    
    public static void descriptografarArquivo(File inputFile) throws Exception {
        // Configurar o cipher AES no modo CBC com padding PKCS5
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY, "AES");

        // Criação do arquivo temporário para a descriptografia
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(tempFile)) {

            // Ler o IV no início do arquivo criptografado
            byte[] iv = new byte[16];
            if (fis.read(iv) != iv.length) {
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

        // Substitui o arquivo criptografado pelo arquivo original
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            throw new Exception("Erro ao substituir o arquivo criptografado pelo original.");
        }

        System.out.println("Arquivo descriptografado com sucesso: " + inputFile.getAbsolutePath());
    }


    
    
    
    
    
}
