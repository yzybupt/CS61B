public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        if (word == null) {
            return null;
        }
        int number = word.length();
        LinkedListDeque<Character> list = new LinkedListDeque<>();
        for (int i = 0; i < number; i++) {
            list.addLast(word.charAt(i));
        }
        return list;
    }

    private boolean isPalindromehelper(Deque list) {
        if (list == null || list.isEmpty() || list.size() == 1) {
            return true;
        } else {
            return list.removeFirst() == list.removeLast() && isPalindromehelper(list);
        }
    }

    public boolean isPalindrome(String word) {
        LinkedListDeque list = (LinkedListDeque) wordToDeque(word);
        /**if (list == null || list.isEmpty()) {
            return true;
        } else {
            for (int i = 0; i < list.size() / 2; i++) {
                if(list.get(i) != list.get(list.size() - 1 - i)) {
                    return false;
                }
            }
            return true;
        }*/
        return isPalindromehelper(list);
    }

    private boolean isPalindromehelper1(Deque list, CharacterComparator cc) {
        if (list == null || list.isEmpty() || list.size() == 1) {
            return true;
        } else {
            return cc.equalChars((char) list.removeFirst(), (char) list.removeLast())
                    && isPalindromehelper1(list, cc);
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        LinkedListDeque list = (LinkedListDeque) wordToDeque(word);
        return isPalindromehelper1(list, cc);
    }

}
