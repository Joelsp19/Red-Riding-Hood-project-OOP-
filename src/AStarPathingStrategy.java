import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<Point>();

        PriorityQueue<Node> openList = new PriorityQueue();
        HashMap<Point,Node> oList = new HashMap<Point,Node>();  //used for quick lookup
        HashMap<Point,Node> cList = new HashMap<Point,Node>();  //used for quick lookup

        Node curNode = new Node(null,start,0,0,0);

        openList.add(curNode);
        oList.put(curNode.getCur(),curNode);

        curNode = openList.poll(); //removes the starting node from the openList and sets it to the current node
        oList.remove(curNode.getCur());

        while( curNode != null && !withinReach.test(curNode.getCur(),end)) {
            for (Point neighbor : potentialNeighbors.apply(curNode.getCur()).filter(s -> !cList.containsKey(s)).filter(canPassThrough).collect(Collectors.toList())) {
                //determine distance from start node(g-value) --> grab current nodes g-value and add 1
                //if neighbor in openList, then compare g-values
                //determine h value (distance to end value)
                //add g and h to get an f value
                //save prior node (cur node)
                //add Node to openList

                boolean inOpenList = false;
                int gVal = curNode.getG() + 1;
                if (oList.containsKey(neighbor)) {
                    if (gVal > oList.get(neighbor).getG()) {
                        //remove Node from openList and oList, so we can add the new, updated one
                        Point p = curNode.getCur();
                        openList.remove(openList.stream().filter(x -> x.getCur().equals(p))); //checks equality based on position
                        oList.remove(p);
                    }else{
                        //don't add a new node to the openLists
                        inOpenList = true;
                    }
                }

                if (!inOpenList) {
                    int hVal = distanceSquared(neighbor, end);
                    int fVal = gVal + hVal;
                    Node n = new Node(curNode.getCur(), neighbor, fVal, hVal, gVal);

                    openList.add(n);
                    oList.put(n.getCur(), n);
                }
            }

            //move current node to closedList
            cList.put(curNode.getCur(), curNode);
            //choose a node with the smallest f-value and make current node
            //if there are elements within the oList(and therefore the openList)
            if (!oList.isEmpty()) {
                curNode = openList.poll(); //sets the new current Node
                oList.remove(curNode.getCur());

            }else{
                //path hasn't been found and there are no more elements in oList...
                //no way to reach the target
                return path;
            }
        }

        //traverse through the previous nodes
        LinkedList<Point> reversePath = new LinkedList<Point>();
        while (curNode.getPrev()!= null){
            reversePath.add(curNode.getCur());
            curNode = cList.get(curNode.getPrev());
        }
        //reverses the reversed path to get the correct order of the path
        for (int i=reversePath.size()-1; i>= 0; i--){
            path.add(reversePath.get(i));
        }
        return path;
    }

    private int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }


}
