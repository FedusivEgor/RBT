public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

    public Node<T> parent;
    public Node<T> right;
    public Node<T> left;
    public T key;
    public boolean color;

    public Node (boolean color, T key) {
        this.color = color;
        parent = right = left = null;
        this.key = key;
    }

    @Override
    public int compareTo(Node<T> o) {
        return this.key.compareTo(o.key);
    }
    public String toString () {return Integer.toString((Integer) this.key);}
}
