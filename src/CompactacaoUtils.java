import java.io.*;
import java.util.zip.*;

public class CompactacaoUtils {

    public static void compactar(File arquivo) throws IOException {
        if (!arquivo.exists() || !arquivo.isFile()) {
            throw new IllegalArgumentException("O arquivo especificado não é válido.");
        }

        File arquivoCompactado = new File(arquivo.getParent(), arquivo.getName() + ".zip");

        try (FileOutputStream fos = new FileOutputStream(arquivoCompactado);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(arquivo)) {

            ZipEntry zipEntry = new ZipEntry(arquivo.getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, bytesRead);
            }
        }

        if (!arquivo.delete()) {
            throw new IOException("Não foi possível excluir o arquivo original.");
        }
    }

    public static void descompactar(File arquivoCompactado) throws IOException {
        if (!arquivoCompactado.exists() || !arquivoCompactado.isFile() || !arquivoCompactado.getName().endsWith(".zip")) {
            throw new IllegalArgumentException("O arquivo especificado não é um arquivo ZIP válido.");
        }

        String nomeArquivoOriginal = arquivoCompactado.getName().replace(".zip", "");
        File arquivoDescompactado = new File(arquivoCompactado.getParent(), nomeArquivoOriginal);

        try (FileInputStream fis = new FileInputStream(arquivoCompactado);
             ZipInputStream zis = new ZipInputStream(fis);
             FileOutputStream fos = new FileOutputStream(arquivoDescompactado)) {

            ZipEntry zipEntry = zis.getNextEntry();
            if (zipEntry == null || !zipEntry.getName().equals(nomeArquivoOriginal)) {
                throw new IOException("O arquivo ZIP não contém o arquivo esperado.");
            }

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        if (!arquivoCompactado.delete()) {
            throw new IOException("Não foi possível excluir o arquivo ZIP.");
        }
    }
}
