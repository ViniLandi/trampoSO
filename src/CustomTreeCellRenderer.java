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
        Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (node.isLeaf()) {
            if (node.getChildCount() == 0 && node.getParent() != null && treeUtils.getFileByNode(node).isDirectory()) {
                setIcon(getDefaultClosedIcon());
            }
        }

        return c;
    }

}
