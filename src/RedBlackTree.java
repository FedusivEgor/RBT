public class RedBlackTree<T extends Comparable<T>> {

    public Node<T> root;

    public RedBlackTree (Node<T> n) {root = n;}

    public void insert (T key) {
        Node<T> newN = new Node<>(true, key);
        if (root == null) { root = newN; return; }
        else {
            Node<T> temp = root;
            Node<T> last = null;
            while (temp != null) {
                last = temp;
                if (newN.compareTo(temp) < 0) temp = temp.left;
                else temp = temp.right;
            }
            newN.parent = last;
            if (newN.compareTo(last) < 0) last.left = newN;
            else last.right = newN;
        }
        fixInsertion(newN);
    }

    public void fixInsertion (Node<T> node) {
        if (node == root) {node.color = false; return;}
        // пока отец красного цвета
        while (node.parent != null && node.parent.color) {
            if (node.parent.parent.left != null && node.parent.parent.right != null) {
                if (node.parent.parent.right == node.parent) {
                    if (node.parent.parent.left.color) {
                        node.parent.color = false;
                        node.parent.parent.left.color = false;
                        node.parent.parent.color = true;
                        node = node.parent.parent;
                    } else {
                        if (node == node.parent.left) {
                            rightRotate(node.parent);
                            node.parent.color = true;
                            node.color = false;
                            leftRotate(node.parent);
                        } else {
                            node.parent.parent.color = true;
                            node.parent.color = false;
                            leftRotate(node.parent.parent);
                        }
                    }
                } else {
                    if (node.parent.parent.right.color) {
                        node.parent.color = false;
                        node.parent.parent.right.color = false;
                        node.parent.parent.color = true;
                        node = node.parent.parent;
                    } else {
                        if (node == node.parent.right) {
                            leftRotate(node.parent);
                            node.parent.color = true;
                            node.color = false;
                            rightRotate(node.parent);
                        } else {
                             node.parent.parent.color = true;
                             node.parent.color = false;
                             rightRotate(node.parent.parent);
                        }
                    }
                }
            } else {
                if (node.parent == node.parent.parent.left) {
                    if (node == node.parent.right) {
                        leftRotate(node.parent);
                        node.parent.color = true;
                        node.color = false;
                        rightRotate(node.parent);
                    } else {
                        node.parent.parent.color = true;
                        node.parent.color = false;
                        rightRotate(node.parent.parent);
                    }
                } else {
                    if (node == node.parent.left) {
                        rightRotate(node.parent);
                        node.parent.color = false;
                        node.color = false;
                        leftRotate(node.parent);
                    } else {
                        node.parent.parent.color = true;
                        node.parent.color = false;
                        leftRotate(node.parent.parent);
                    }
                }
            }
        }
        root.color = false;
    }

    public void rightRotate (Node<T> node) {
        Node<T> temp = node.left;
        node.left = temp.right;
        if (temp.right != null) temp.right.parent = node;
        temp.parent = node.parent;
        if (node != root) {
            if (node == node.parent.left) node.parent.left = temp;
            else node.parent.right = temp;
        }
        temp.right = node;
        node.parent = temp;
        if (node == root) root = temp;
    }

    public void leftRotate (Node<T> node) {
        Node<T> temp = node.right;
        node.right = temp.left;
        if (temp.left != null) temp.left.parent = node;
        temp.parent = node.parent;
        if (node != root) {
            if (node == node.parent.left) node.parent.left = temp;
            else node.parent.right = temp;
        }
        temp.left = node;
        node.parent = temp;
        if (node == root) root = temp;
    }

    public void deleteM (T key) {
        Node<T> temp = root;
        while (temp.key != key) {
            if (temp.key.compareTo(key) < 0) temp = temp.right;
            else temp = temp.left;
        }
        if (temp.left != null && temp.right != null) {
            temp = temp.right;
            while (temp.left != null) temp = temp.left;
            if (temp.right != null) deleteCaseOneChild(temp);
            else deleteCaseNoChildren(temp);
        } else if (temp.left != null || temp.right != null) deleteCaseOneChild(temp);
        else deleteCaseNoChildren(temp);
    }

    public void deleteCaseOneChild (Node<T> node) {
        // если один ребенок, то он точно красный, а узел точно черный
        // просто переносим ключ из сына в отца, указатель на сына удаляем

        if (node.left != null) {
            node.key = node.left.key;
            node.left = null;
        } else {
            node.key = node.right.key;
            node.right = null;
        }
    }

    public void deleteCaseNoChildren (Node<T> node) {
        // детей нет
        // если узел красный, то просто удаляем его
        // варианты рассматриваем в случае черного цвета узла

        if (node.color) { // узел красный
            if (node.parent.left == node) node.parent.left = null;
            else node.parent.right = null;
        } else {
            // узел черный
            // у черного узла точно есть брат
            // если брат черный, то его потомки точно красные
            Node<T> father = node.parent;
            if (father.left == node) {
                // у брата один красный потомок
                if (!father.right.color) {
                    if (father.right.left != null && father.right.right == null) {
                        father.left = null;
                        father.right.left.color = false;
                        father.right.color = father.color;
                        father.color = false;
                        father.right.parent = father.parent;
                        father.parent = father.right;
                        father.parent.right = father;
                        T temp = father.parent.right.key;
                        father.parent.right.key = father.parent.left.key;
                        father.parent.left.key = temp;
                        if (root == father) root = father.parent;
                    } else if (father.right.right != null && father.right.left == null) {
                        father.left = null;
                        father.right.right.color = false;
                        father.right.color = father.color;
                        father.color = false;
                        father.right.parent = father.parent;
                        father.parent = father.right;
                        father.parent.left = father;
                        if (root == father) root = father.parent;
                    }
                    // два красных потомка
                    else if (father.right.right != null) {
                        father.left = null;
                        father.right.color = father.color;
                        father.color = father.right.right.color = false;
                        leftRotate(father);
                    } else { // у брата потомков нет
                        if (father.color) {
                            father.left = null;
                            father.right.color = true;
                            father.color = false;
                        } else {
                            father.left = null;
                            father.right.color = true;
                            leftRotate(father.parent);
                        }
                    }
                } else { // брат красный
                    father.right.left.color = true;
                    leftRotate(father);
                }
            } else {
                // у брата один красный потомок
                if (!father.left.color) {
                    if (father.left.left != null && father.left.right == null) {
                        father.right = null;
                        father.left.left.color = false;
                        father.left.color = father.color;
                        father.color = false;
                        father.left.parent = father.parent;
                        father.parent = father.left;
                        father.parent.right = father;
                        T temp = father.parent.right.key;
                        father.parent.right.key = father.parent.left.key;
                        father.parent.left.key = temp;
                        if (root == father) root = father.parent;
                    } else if (father.right.right != null && father.right.left == null) {
                        father.right = null;
                        father.left.right.color = false;
                        father.left.color = father.color;
                        father.color = false;
                        father.left.parent = father.parent;
                        father.parent = father.left;
                        father.parent.left = father;
                        if (root == father) root = father.parent;
                    }
                    // два красных потомка
                    else if (father.left.right != null) {
                        father.right = null;
                        father.left.color = father.color;
                        father.color = father.left.right.color = false;
                        rightRotate(father);
                    } else { // у брата потомков нет
                        if (father.color) {
                            father.right = null;
                            father.left.color = true;
                            father.color = false;
                        } else {
                            father.right = null;
                            father.left.color = true;
                            rightRotate(father.parent);
                        }
                    }
                } else { // брат красный
                    father.left.left.color = true;
                    rightRotate(father);
                }
            }
        }
    }

}
