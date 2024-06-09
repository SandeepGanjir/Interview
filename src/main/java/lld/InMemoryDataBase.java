package lld;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class InMemoryDataBase {
    static class Operation {
        String type;
        String key;
        String value;
        Operation(String type, String key, String value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }
    }


    private final Map<String, String> data = new HashMap<>();
    private Stack<Stack<Operation>> undoStack = new Stack<>();

    public InMemoryDataBase() {}

    public void add(String key, String value) {
        if (!undoStack.isEmpty()) {
            Operation newAdd = new Operation("add", key, data.get(key));
            undoStack.peek().add(newAdd);
        }
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public void delete(String key) {
        if (data.containsKey(key) && !undoStack.isEmpty()) {
            Operation newDelete = new Operation("delete", key, data.get(key));
            undoStack.peek().add(newDelete);
        }
        data.remove(key);
    }

    public void beginTransaction() {
        Stack<Operation> newTxn = new Stack<>();
        undoStack.push(newTxn);
    }

    public void rollback() {
        if (!undoStack.isEmpty()) {
            Stack<Operation> undoOps = undoStack.pop();
            while (!undoOps.isEmpty()) {
                Operation op = undoOps.pop();
                switch(op.type) {
                    case "add": _undoAdd(op); break;
                    case "delete": _undoDelete(op); break;
                }
            }
        } else {
            System.out.println("Nothing to rollback.");
        }
    }

    public void commit() {
        // Single Txn commits all => cleanup undo stack
        undoStack = new Stack<>();
    }

    private void _undoAdd(Operation op) {
        if (op.value == null) {
            data.remove(op.key);
        } else {
            data.put(op.key, op.value);
        }
    }
    private void _undoDelete(Operation op) {
        data.put(op.key, op.value);
    }

    public static void main(String[] args) {
        InMemoryDataBase txns = new InMemoryDataBase();

        txns.add("Sandeep", "Kumar");
        System.out.println(txns.get("Sandeep"));
        System.out.println(txns.get("Archana"));
        txns.beginTransaction();
        txns.add("Sandeep", "Ganjir");
        System.out.println(txns.get("Sandeep"));
        txns.beginTransaction();
        txns.delete("Sandeep");
        System.out.println(txns.get("Sandeep"));
        txns.rollback();
        System.out.println(txns.get("Sandeep"));
        txns.commit();
        txns.rollback();
        System.out.println(txns.get("Sandeep"));
    }
}
