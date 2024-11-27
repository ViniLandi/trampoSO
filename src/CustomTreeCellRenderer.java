import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = 2306358440603854342L;
	private TreeUtils treeUtils;
	
	public CustomTreeCellRenderer(TreeUtils treeUtils) {
		this.treeUtils = treeUtils;
	}
	
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        // Configuração padrão do nó
        Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        // Converter para DefaultMutableTreeNode
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        // Verificar se o nó é uma folha (arquivo ou pasta vazia)
        if (node.isLeaf()) {
            // Verifica se a "folha" deve ser considerada uma pasta vazia
            if (node.getChildCount() == 0 && node.getParent() != null && treeUtils.getFileByNode(node).isDirectory()) {
                setIcon(getDefaultClosedIcon()); // Ícone de pasta fechada
            }
        }

        return c;
    }

}
