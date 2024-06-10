package linkedlist;

/**
 * Input : L0 - > L1 -> L2 -> L3 -> -- Ln-1 -> Ln
 * Output: L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2.....
 */
public class AlternateElements {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        AlternateElements ins = new AlternateElements();
        Node head = ins.input();
        ins.print(head);
        Node arranged = ins.reArrange(head);
        System.out.println("\nAfter Rearranging");
        ins.print(arranged);
    }

    public void print(Node head) {
        Node node = head;
        while (node != null) {
            System.out.print(node.val + " -> ");
            node = node.next;
        }
    }

    public Node input() {
        Node head = new Node("L" + 0);
        Node next = head;
        for (int i = 1; i < 11; i++) {
            next.next = new Node("L" + i);
            next = next.next;
        }
        return head;
    }

    private Node getMid(Node head) {
        Node one = head, two = head, last = head;
        while (two != null) {
            last = one;
            one = one.next;
            two = two.next;
            if (two != null) {
                two = two.next;
            }
        }
        if (last != null) last.next = null;
        return one;
    }

    private Node reverseSecondHalf(Node head) {
        Node mid = head;
        Node secondhalf = mid;

        if (mid != null) {
            mid = mid.next;
            secondhalf.next = null;
            while (mid != null) {
                Node tmp = mid.next;
                mid.next = secondhalf;
                secondhalf = mid;
                mid = tmp;
            }
        }
        return secondhalf;
    }

    private Node merge(Node firstHalf, Node seconfhalf) {
        Node curNode1 = firstHalf;
        System.out.println("\n 1st half");
        print(firstHalf);
        Node curNode2 = seconfhalf;
        System.out.println("\n 2nd half");
        print(seconfhalf);
        while (curNode2 != null) {
            Node tmp1 = curNode1.next;
            Node tmp2 = curNode2.next;
            curNode1.next = curNode2;
            curNode2.next = tmp1;
            curNode1 = tmp1;
            curNode2 = tmp2;
        }
        return firstHalf;
    }

    public Node reArrange(Node head) {
        Node mid = getMid(head);
        Node firstHalf = head;
        Node secondhalf = reverseSecondHalf(mid);

        merge(firstHalf, secondhalf);
        return head;
    }
}

class Node {
    String val;
    Node next = null;

    Node(String v) {
        val = v;
    }
}