/* Generated By:JJTree: Do not edit this line. ASTPreDecrementExpression.java */

package net.sourceforge.pmd.ast;

public class ASTPreDecrementExpression extends SimpleJavaNode {
    public ASTPreDecrementExpression(int id) {
        super(id);
    }

    public ASTPreDecrementExpression(JavaParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
