import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeUtils {

//	public static long leitura = 0;
	
	public DefaultMutableTreeNode atualizar(DefaultMutableTreeNode nodePai) {
		if (nodePai != null)
	    	return loadDirectoryTree(nodePai, getFileByNode(nodePai));
		return null;
	}
	
	public DefaultMutableTreeNode buildTree(File file) {
        long size = calculateSize(file);
        
	    DefaultMutableTreeNode node = new DefaultMutableTreeNode(file + " (" + formatFileSize(size) + ")"); // NÃO MEXER NISTO (APENAS SE SOUBER O QUE ESTÁ FAZENDO)

	    if (file.isDirectory()) {
	    	if (file.listFiles() != null) {
		        for (File child : file.listFiles()) {
		            node.add(buildTree(child));
		        }
	    	} 
	    }
//	    else {
//	        leitura += size;
//	        System.out.println(leitura);
//	    }
	    
	    return node;	    
	}
	
	public long calculateSize(File file) {
	    if (file.isFile()) {
	        return file.length();
	    }
	    long size = 0;
	    if (file.listFiles() != null) {
		    for (File child : file.listFiles()) {
		        size += calculateSize(child);
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
//		leitura = 0;
	    nodePai = buildTree(root);
	    return nodePai;
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
