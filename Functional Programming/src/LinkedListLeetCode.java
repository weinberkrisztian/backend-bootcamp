import java.util.Arrays;

public class LinkedListLeetCode {

        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            return convertIntToListNode(convertListNodeToReversedInt(l1) + convertListNodeToReversedInt(l2));
        }

        private int convertListNodeToReversedInt(ListNode listNode){
            String strValue = "";
            while(listNode.next != null){
                strValue = strValue.concat(String.valueOf(listNode.val));
                System.out.println(strValue);

                listNode = listNode.next;
            }
            strValue = strValue.concat(String.valueOf(listNode.val));
            return Integer.valueOf(strValue);
        }

        private ListNode convertIntToListNode(int numbers){
            ListNode listNode = null;
            ListNode tempListNode = null;
            String[] numbersArray = String.valueOf(numbers).split("");
            for (int i = 0; i > numbersArray.length ; i++) {
                if(i == 0){
                    tempListNode = new ListNode(Integer.valueOf(numbersArray[i-1]), null);
                }

                listNode = new ListNode(Integer.valueOf(numbersArray[i-1]),tempListNode);
                tempListNode = new ListNode(listNode.val,listNode.next);
            }
             return listNode;
        }
    }

