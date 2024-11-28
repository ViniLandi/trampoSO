import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeUtils {

	
	public DefaultMutableTreeNode atualizar(DefaultMutableTreeNode nodePai) {
		if (nodePai != null)
	    	return loadDirectoryTree(nodePai, getFileByNode(nodePai));
		return null;
	}
	
	public DefaultMutableTreeNode buildTree(File file, Boolean root) {
        long size = calculateSize(file, root);
        
	    DefaultMutableTreeNode node = new DefaultMutableTreeNode(file + " (" + formatFileSize(size) + ")"); // NÃO MEXER NISTO (APENAS SE SOUBER O QUE ESTÁ FAZENDO)

	    if (file.isDirectory()) {
	    	if (file.listFiles() != null) {
		        for (File child : file.listFiles()) {
		            node.add(buildTree(child, false));
		        }
	    	} 
	    }
	    
	    return node;	    
	}
	
	public long calculateSize(File file, Boolean root) {
	    if (file.isFile()) {
	        return file.length();
	    }
	    long size = 0;
//	    root && 
	    if (file.listFiles() != null) {
		    for (File child : file.listFiles()) {
		        size += calculateSize(child, root);
		    }
	    }
	    return size;
	}

	public String formatFileSize(long size) {
	    if (size < 1024) return size + " B";
	    int exp = (int) (Math.log(size) / Math.log(1024));
	    char unit = "KMGTPE".charAt(exp - 1);
	    return String.format("%.1f %sB", size / Math.pow(1024, exp), unit);
	}

	public DefaultMutableTreeNode loadDirectoryTree(DefaultMutableTreeNode nodePai, File root) {
	    nodePai = buildTree(root, true);
	    return nodePai;
	}
	
    public String getFileExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "sem_extensao";
    }
    
    public static String adicionarExtensao(String fileName, String novaExtensao) {
        int lastDotIndex = fileName.lastIndexOf(" (");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(0, lastDotIndex) + "." + novaExtensao + fileName.substring(lastDotIndex);
        }
        return fileName;
    }
	
	public File getFileByNode(DefaultMutableTreeNode node) {
		File retorno = null;
		String nodeString = node.getUserObject().toString();
        int lastDotIndex = nodeString.lastIndexOf(" (");
        if (lastDotIndex > 0 && lastDotIndex < nodeString.length() - 1) {
        	retorno = new File(nodeString.substring(0, lastDotIndex));
        }
        return retorno;
	}
}
