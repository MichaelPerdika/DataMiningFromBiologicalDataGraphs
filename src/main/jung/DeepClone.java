package main.jung;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public final class DeepClone {

    private DeepClone(){}

    public static <X> X deepClone(final X input) {
        if (input == null) {
            return input;
        } else if (input instanceof Map<?, ?>) {
            return (X) deepCloneMap((Map<?, ?>) input);
        } else if (input instanceof Collection<?>) {
            return (X) deepCloneCollection((Collection<?>) input);
        } else if (input instanceof Object[]) {
            return (X) deepCloneObjectArray((Object[]) input);
        } else if (input.getClass().isArray()) {
            return (X) clonePrimitiveArray((Object) input);
        } else if (input instanceof DirectedGraph<?, ?>) {
            return (X) deepCloneDirectedGraph((DirectedGraph<Integer, MyEdge>) input);
        }
        

        return input;
    }
    

    private static Object clonePrimitiveArray(final Object input) {
        final int length = Array.getLength(input);
        final Object copy = Array.newInstance(input.getClass().getComponentType(), length);
        // deep clone not necessary, primitives are immutable
        System.arraycopy(input, 0, copy, 0, length);
        return copy;
    }

    private static <E> E[] deepCloneObjectArray(final E[] input) {
        final E[] clone = (E[]) Array.newInstance(input.getClass().getComponentType(), input.length);
        for (int i = 0; i < input.length; i++) {
            clone[i] = deepClone(input[i]);
        }

        return clone;
    }

    private static <E> Collection<E> deepCloneCollection(final Collection<E> input) {
        Collection<E> clone;
        // this is of course far from comprehensive. extend this as needed
        if (input instanceof LinkedList<?>) {
            clone = new LinkedList<E>();
        } else if (input instanceof SortedSet<?>) {
            clone = new TreeSet<E>();
        } else if (input instanceof Set) {
            clone = new HashSet<E>();
        } else {
            clone = new ArrayList<E>();
        }

        for (E item : input) {
            clone.add(deepClone(item));
        }

        return clone;
    }

    private static <K, V> Map<K, V> deepCloneMap(final Map<K, V> map) {
        Map<K, V> clone;
        // this is of course far from comprehensive. extend this as needed
        if (map instanceof LinkedHashMap<?, ?>) {
            clone = new LinkedHashMap<K, V>();
        } else if (map instanceof TreeMap<?, ?>) {
            clone = new TreeMap<K, V>();
        } else {
            clone = new HashMap<K, V>();
        }

        for (Entry<K, V> entry : map.entrySet()) {
            clone.put(deepClone(entry.getKey()), deepClone(entry.getValue()));
        }

        return clone;
    }
    
    private static DirectedGraph<Integer, MyEdge> deepCloneDirectedGraph(
    		final DirectedGraph<Integer, MyEdge> graph) {
    	DirectedGraph<Integer, MyEdge> clone;
    	// this is of course far from comprehensive. extend this as needed
        if (graph instanceof DirectedSparseMultigraph<?, ?>) {
            clone = new DirectedSparseMultigraph<Integer, MyEdge>();
        } else {
        	System.out.println("error in deep clone for DirectedGraph");
            System.exit(1);
            return null;
        }
        
        Collection<MyEdge> edgeCollection = graph.getEdges();
        for ( MyEdge curEdge : edgeCollection) {
            clone.addEdge(curEdge, curEdge.getStartNode(), curEdge.getEndNode());
            System.out.println("curEdge"+"["+ curEdge.getStartNode() +", "+ curEdge.getEndNode()+"]");
            System.out.println("clone"+"["+ clone.findEdge(curEdge.getStartNode(), curEdge.getEndNode()).getStartNode() +", "+ clone.findEdge(curEdge.getStartNode(), curEdge.getEndNode()).getEndNode()+"]");
        }

        return clone;
	}
}
