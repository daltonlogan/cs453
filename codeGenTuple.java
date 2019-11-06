import java.util.List;
import java.util.TreeMap;

public class codeGenTuple {
    private List<Tao> theThreeAddressList;
    private TreeMap< String, Symbol > theTable;
    private String theName;

    public codeGenTuple ( List<Tao> aThreeAddrList, TreeMap< String, Symbol > aTable, String aName )
    {
        theThreeAddressList = aThreeAddrList;
        theTable = aTable;
        theName = aName;
    }

    public List<Tao> getTheThreeAddressList() {
        return theThreeAddressList;
    }

    public TreeMap<String, Symbol> getTheTable() {
        return theTable;
    }

    public String getTheName() {
        return theName;
    }

    public void setTheThreeAddressList(List<Tao> aThreeAddressList) {
        theThreeAddressList = aThreeAddressList;
    }

    public void setTheTable(TreeMap<String, Symbol> aTable) {
        theTable = aTable;
    }

    public void setTheName(String aName) {
        theName = aName;
    }
}
