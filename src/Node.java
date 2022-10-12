
public class Node implements Comparable<Node>{
    private Point prev;
    private Point cur;
    private int f;
    private int h;
    private int g;

    public Node(Point prev,Point cur,int f,int h,int g){
        this.prev = prev;
        this.cur = cur;
        this.f = f;
        this.h = h;
        this.g = g;
    }

    public Point getPrev() {
        return prev;
    }

    public void setPrev(Point prev) {
        this.prev = prev;
    }

    public Point getCur() {
        return cur;
    }

    public void setCur(Point cur) {
        this.cur = cur;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int compareTo(Node n){
        if (this.f < n.getF()){
            return -1;
        }else if (this.f > n.getF()){
            return +1;
        }else{
            return 0;
        }
    }

    public String toString()
    {
        return "prev: " + prev + " cur: " + cur.toString() + " f: " + f;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + prev.hashCode();
        result = result * 31 + cur.hashCode();
        result = result * 31 + f;
        result = result * 31 + h;
        result = result * 31 + g;

        return result;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return f == node.f && h == node.h && g == node.g && prev.equals(node.prev) && cur.equals(node.cur);
    }
}