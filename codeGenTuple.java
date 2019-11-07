import java.util.List;

public class codeGenTuple {
    private List<Tao> theThreeAddressList;
    private Table theTable;
    private String theName;

    public codeGenTuple ( List<Tao> aThreeAddrList, Table aTable, String aName )
    {
        theThreeAddressList = aThreeAddrList;
        theTable = aTable;
        theName = aName;
    }

    public List<Tao> getTheThreeAddressList() {
        return theThreeAddressList;
    }

    public Table getTheTable() {
        return theTable;
    }

    public String getTheName() {
        return theName;
    }

    public void setTheThreeAddressList(List<Tao> aThreeAddressList) {
        theThreeAddressList = aThreeAddressList;
    }

    public void setTheTable(Table aTable) {
        theTable = aTable;
    }

    public void setTheName(String aName) {
        theName = aName;
    }
}
