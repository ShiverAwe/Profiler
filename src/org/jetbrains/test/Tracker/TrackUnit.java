package org.jetbrains.test.Tracker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Vladimir Shefer on 05.05.2017.
 */
public class TrackUnit {
    private final TrackUnit invoker;
    private ArrayList<TrackUnit> invoked = new ArrayList<>();
    private final String methodName;
    private int callCount;
    // Length of path from this node to the furthest child.
    private int internalDepth = 0;
    // Length of path from root to this node.
    private int externalHeight;

    public int getInternalDepth(){
        return internalDepth;
    }

    public String getMethodName(){
        return this.methodName;
    }

    public int getCallCount(){
        return callCount;
    }

    public TrackUnit getInvoker(){
        return this.invoker;
    }

    public TrackUnit(TrackUnit invoker, String methodName){
        this.invoker = invoker;
        this.methodName = methodName;
        callCount = 1;
        if (invoker != null)
            externalHeight = invoker.externalHeight + 1;
        else externalHeight = 0;
    }

    /**
     * Increases the callCount of invoked method
     * OR Creates new TrackUnit, if method never called before.
     * @param methodName
     * @return TrackUnit of invoked method.
     */
    public TrackUnit getInvoked(String methodName) {
        for (TrackUnit unit : invoked){
            if (unit.getMethodName().compareTo(methodName) == 0)
            {
                unit.inc();
                return unit;
            }
        }
        TrackUnit newCall = new TrackUnit(this, methodName);
        invoked.add(newCall);
        updateInternalDepth(1);
        return newCall;
    }

    /**
     *  Used to update max inner depth for all parent nodes.
     * @param internalDepth
     */
    public void updateInternalDepth(int internalDepth){
        // if node has more "deep" children - do nothing. 
        if (internalDepth < this.internalDepth) return;
        
        this.internalDepth = internalDepth;
        if (invoker != null) {
            invoker.updateInternalDepth(internalDepth + 1 );
        }
    }

    public void inc(){
        this.callCount++;
    }

    /**
     * Sorts invoked methods list by max inner depth for more convenient display
     * Сортитрует вызванные методы по глубине внутреннего дерева для удобного отображения в консоли.
     */
    public void sortByDepth(){
        invoked.sort(Comparator.comparing(TrackUnit::getInternalDepth));
    }

    /**
     * Used to print root-node.
     * Запускает рекурсивный вывод дерева в консоль.
     */
    public void printTrack(){
        System.out.println();
        printTrack("");
    }
    
    /**
     *  Recursively prints call-tree.
     *  Every children prints right from parent.
     *  Рекурсивно выводит в консоль дерево вызовов.
     *  Каждый ребенок выводится правее родителя.
     * @param placeholder remains from parent node to denote hierarchy.
     *        отступ добавляется каждым родителем, чтобы графически обозначить иерархию.
     *
     */
    public void printTrack(String placeholder){
        //sortByDepth();
        int branchCount = invoked.size();
        // for list we do not draw brackets
        if (branchCount == 0) {
            System.out.println( placeholder + "|>--" +getInfo() + "--");
        } else // for nodes with children we prepare "brackets"
        {
            System.out.println( placeholder +"|\\--" +getInfo() + "--"); // opening block
            for (int i = 0; i < branchCount; i++) {
                TrackUnit unit = invoked.get(i);
                unit.printTrack(placeholder + "| ");
            }
            //System.out.println( placeholder + "|/-----"); // closing block // disabled to save vertical space
        }
    }

    /**
     * @return Information about method. Used for printing to console.
     */
    public String getInfo(){
        return "" + methodName + "{" + externalHeight + "/" + internalDepth + ", " + callCount + " times}";
    }

    /**
     * Recursively builds call-tree into XMLBuilder-Document
     * @param element of XMLBuilder-Document, where to put call-tree.
     */
    public void putToXMLElement(Element element){
        Document document = element.getOwnerDocument();
        Element elem = document.createElement("call");
        elem.setAttribute("method", methodName);
        elem.setAttribute("count", callCount+"");
        //elem.setAttribute("internalDepth", internalDepth +"");
        //elem.setAttribute("externalHeight", externalHeight +"");
        element.appendChild(elem);
        for (int i = 0; i < invoked.size(); i++) {
            TrackUnit unit = invoked.get(i);
            unit.putToXMLElement(elem);
        }
    }

}
