/* Generated By:JJTree: Do not edit this line. ASTAllocationExpression.java */

package net.sourceforge.pmd.ast;

public class ASTAllocationExpression extends SimpleJavaNode {
    public ASTAllocationExpression(int id) {
        super(id);
    }

    public ASTAllocationExpression(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
